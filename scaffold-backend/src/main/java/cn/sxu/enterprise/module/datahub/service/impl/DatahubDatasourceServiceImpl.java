package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubDatasourceTestRequest;
import cn.sxu.enterprise.module.datahub.entity.DatahubDatasource;
import cn.sxu.enterprise.module.datahub.mapper.DatahubDatasourceMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubDatasourceService;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourcePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTestVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatahubDatasourceServiceImpl implements DatahubDatasourceService {

    private final DatahubDatasourceMapper datahubDatasourceMapper;

    public DatahubDatasourceServiceImpl(DatahubDatasourceMapper datahubDatasourceMapper) {
        this.datahubDatasourceMapper = datahubDatasourceMapper;
    }

    @Override
    public PageResult<DatahubDatasourcePageVO> pageDatasources(DatahubDatasourcePageQuery query) {
        Long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10L : query.getPageSize();

        LambdaQueryWrapper<DatahubDatasource> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getDatasourceCode()), DatahubDatasource::getDatasourceCode, query.getDatasourceCode())
                .like(StringUtils.hasText(query.getDatasourceName()), DatahubDatasource::getDatasourceName, query.getDatasourceName())
                .eq(StringUtils.hasText(query.getDatasourceType()), DatahubDatasource::getDatasourceType, query.getDatasourceType())
                .like(StringUtils.hasText(query.getHost()), DatahubDatasource::getHost, query.getHost())
                .like(StringUtils.hasText(query.getDatabaseName()), DatahubDatasource::getDatabaseName, query.getDatabaseName())
                .eq(StringUtils.hasText(query.getEnvType()), DatahubDatasource::getEnvType, query.getEnvType())
                .like(StringUtils.hasText(query.getOwnerName()), DatahubDatasource::getOwnerName, query.getOwnerName())
                .eq(query.getTestStatus() != null, DatahubDatasource::getTestStatus, query.getTestStatus())
                .eq(query.getStatus() != null, DatahubDatasource::getStatus, query.getStatus())
                .orderByDesc(DatahubDatasource::getCreateTime)
                .orderByDesc(DatahubDatasource::getId);

        Page<DatahubDatasource> page = datahubDatasourceMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);

        List<DatahubDatasourcePageVO> records = page.getRecords()
                .stream()
                .map(DatahubDatasourcePageVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    @Override
    public DatahubDatasourceTestVO testConnection(DatahubDatasourceTestRequest request) {
        LocalDateTime testTime = LocalDateTime.now();
        long start = System.currentTimeMillis();

        DatahubDatasourceTestVO vo = new DatahubDatasourceTestVO();
        vo.setDatasourceType(request.getDatasourceType());
        vo.setJdbcUrl(request.getJdbcUrl());
        vo.setTestTime(testTime);

        try (Connection connection = DriverManager.getConnection(
                request.getJdbcUrl(),
                request.getUsername(),
                request.getPassword()
        )) {
            long costTime = System.currentTimeMillis() - start;
            vo.setTestStatus(1);
            vo.setTestMessage("连接成功");
            vo.setCostTime(costTime);
            vo.setDatabaseProductName(connection.getMetaData().getDatabaseProductName());
            vo.setDatabaseProductVersion(connection.getMetaData().getDatabaseProductVersion());
        } catch (Exception ex) {
            long costTime = System.currentTimeMillis() - start;
            vo.setTestStatus(2);
            vo.setTestMessage("连接失败：" + ex.getMessage());
            vo.setCostTime(costTime);
        }

        return vo;
    }
}
