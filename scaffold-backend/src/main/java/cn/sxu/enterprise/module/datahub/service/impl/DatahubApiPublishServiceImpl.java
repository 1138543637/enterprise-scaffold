package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubApiPublishCreateRequest;
import cn.sxu.enterprise.module.datahub.entity.DatahubApiPublish;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataTable;
import cn.sxu.enterprise.module.datahub.mapper.DatahubApiPublishMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataTableMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubApiPublishService;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubApiPublishPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DatahubApiPublishServiceImpl implements DatahubApiPublishService {

    private static final DateTimeFormatter CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DatahubApiPublishMapper datahubApiPublishMapper;
    private final DatahubMetadataTableMapper datahubMetadataTableMapper;

    @Override
    public PageResult<DatahubApiPublishPageVO> pageApiPublishes(DatahubApiPublishPageQuery query) {
        Long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10L : query.getPageSize();

        LambdaQueryWrapper<DatahubApiPublish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getApiCode()), DatahubApiPublish::getApiCode, query.getApiCode())
                .like(StringUtils.hasText(query.getApiName()), DatahubApiPublish::getApiName, query.getApiName())
                .like(StringUtils.hasText(query.getDatasourceName()), DatahubApiPublish::getDatasourceName, query.getDatasourceName())
                .like(StringUtils.hasText(query.getTableName()), DatahubApiPublish::getTableName, query.getTableName())
                .eq(StringUtils.hasText(query.getRequestMethod()), DatahubApiPublish::getRequestMethod, query.getRequestMethod())
                .eq(query.getOnlineStatus() != null, DatahubApiPublish::getOnlineStatus, query.getOnlineStatus())
                .eq(query.getStatus() != null, DatahubApiPublish::getStatus, query.getStatus())
                .orderByDesc(DatahubApiPublish::getCreateTime)
                .orderByDesc(DatahubApiPublish::getId);

        Page<DatahubApiPublish> page = datahubApiPublishMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<DatahubApiPublishPageVO> records = page.getRecords().stream()
                .map(DatahubApiPublishPageVO::fromEntity)
                .toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatahubApiPublishPageVO publishFromTable(DatahubApiPublishCreateRequest request) {
        DatahubMetadataTable table = datahubMetadataTableMapper.selectById(request.getTableId());
        if (table == null || table.getDeleted() != null && table.getDeleted() == 1) {
            throw new BusinessException(500, "元数据表不存在或已删除，不能发布 API");
        }

        String requestMethod = StringUtils.hasText(request.getRequestMethod())
                ? request.getRequestMethod().trim().toUpperCase()
                : "GET";
        if (!"GET".equals(requestMethod) && !"POST".equals(requestMethod)) {
            throw new BusinessException(500, "requestMethod 目前只支持 GET 或 POST");
        }

        String tableName = table.getTableName();
        String apiPath = StringUtils.hasText(request.getApiPath())
                ? normalizeApiPath(request.getApiPath())
                : "/openapi/datahub/" + normalizePathPart(tableName) + "/page";

        Long samePathCount = datahubApiPublishMapper.selectCount(new LambdaQueryWrapper<DatahubApiPublish>()
                .eq(DatahubApiPublish::getApiPath, apiPath));
        if (samePathCount != null && samePathCount > 0) {
            throw new BusinessException(500, "API路径已存在，请换一个 apiPath");
        }

        LocalDateTime now = LocalDateTime.now();
        DatahubApiPublish entity = new DatahubApiPublish();
        entity.setApiCode(generateApiCode());
        entity.setApiName(StringUtils.hasText(request.getApiName()) ? request.getApiName().trim() : tableName + "分页查询API");
        entity.setDatasourceId(table.getDataSourceId());
        entity.setDatasourceCode(null);
        entity.setDatasourceName(table.getDataSourceName());
        entity.setTableId(table.getId());
        entity.setTableCode(table.getTableName());
        entity.setTableName(table.getTableName());
        entity.setTableComment(table.getTableComment());
        entity.setApiPath(apiPath);
        entity.setRequestMethod(requestMethod);
        entity.setPublishType("TABLE_PAGE");
        entity.setOnlineStatus(0);
        entity.setAuthRequired(1);
        entity.setOwnerName(StringUtils.hasText(request.getOwnerName()) ? request.getOwnerName().trim() : "admin");        entity.setPublishTime(now);
        entity.setStatus(0);
        entity.setCreateBy("admin");
        entity.setCreateTime(now);
        entity.setUpdateBy("admin");
        entity.setUpdateTime(now);
        entity.setDeleted(0);
        entity.setRemark(request.getRemark());
        datahubApiPublishMapper.insert(entity);
        return DatahubApiPublishPageVO.fromEntity(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatahubApiPublishPageVO online(Long id) {
        DatahubApiPublish entity = getApiPublishOrThrow(id);
        if (entity.getStatus() != null && entity.getStatus() == 1) {
            throw new BusinessException(500, "API已停用，不能上线");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setOnlineStatus(1);
        entity.setLastOnlineTime(now);
        entity.setUpdateBy("admin");
        entity.setUpdateTime(now);
        datahubApiPublishMapper.updateById(entity);
        return DatahubApiPublishPageVO.fromEntity(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatahubApiPublishPageVO offline(Long id) {
        DatahubApiPublish entity = getApiPublishOrThrow(id);
        LocalDateTime now = LocalDateTime.now();
        entity.setOnlineStatus(0);
        entity.setLastOfflineTime(now);
        entity.setUpdateBy("admin");
        entity.setUpdateTime(now);
        datahubApiPublishMapper.updateById(entity);
        return DatahubApiPublishPageVO.fromEntity(entity);
    }

    private DatahubApiPublish getApiPublishOrThrow(Long id) {
        DatahubApiPublish entity = datahubApiPublishMapper.selectById(id);
        if (entity == null || entity.getDeleted() != null && entity.getDeleted() == 1) {
            throw new BusinessException(500, "API发布记录不存在或已删除");
        }
        return entity;
    }

    private String generateApiCode() {
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "API-" + LocalDateTime.now().format(CODE_TIME_FORMATTER) + "-" + random;
    }

    private String normalizeApiPath(String apiPath) {
        String path = apiPath.trim();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path.replaceAll("/{2,}", "/");
    }

    private String normalizePathPart(String value) {
        if (!StringUtils.hasText(value)) {
            return "table";
        }
        return value.trim().toLowerCase().replaceAll("[^a-z0-9_\\-]", "-");
    }
}
