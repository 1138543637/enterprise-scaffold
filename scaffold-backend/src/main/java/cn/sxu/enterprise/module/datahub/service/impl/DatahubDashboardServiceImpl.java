package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.module.datahub.entity.DatahubApiPublish;
import cn.sxu.enterprise.module.datahub.entity.DatahubDatasource;
import cn.sxu.enterprise.module.datahub.entity.DatahubMaskResult;
import cn.sxu.enterprise.module.datahub.entity.DatahubMaskRule;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataColumn;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataTable;
import cn.sxu.enterprise.module.datahub.entity.DatahubQualityResult;
import cn.sxu.enterprise.module.datahub.entity.DatahubQualityRule;
import cn.sxu.enterprise.module.datahub.entity.DatahubSensitiveField;
import cn.sxu.enterprise.module.datahub.mapper.DatahubApiPublishMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubDatasourceMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMaskResultMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMaskRuleMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataColumnMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataTableMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubQualityResultMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubQualityRuleMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubSensitiveFieldMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubDashboardService;
import cn.sxu.enterprise.module.datahub.vo.DatahubDashboardSummaryVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTypeStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentApiPublishVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentQualityResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveTypeStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatahubDashboardServiceImpl implements DatahubDashboardService {

    private final DatahubDatasourceMapper datahubDatasourceMapper;
    private final DatahubMetadataTableMapper datahubMetadataTableMapper;
    private final DatahubMetadataColumnMapper datahubMetadataColumnMapper;
    private final DatahubQualityRuleMapper datahubQualityRuleMapper;
    private final DatahubQualityResultMapper datahubQualityResultMapper;
    private final DatahubSensitiveFieldMapper datahubSensitiveFieldMapper;
    private final DatahubMaskRuleMapper datahubMaskRuleMapper;
    private final DatahubMaskResultMapper datahubMaskResultMapper;
    private final DatahubApiPublishMapper datahubApiPublishMapper;

    @Override
    public DatahubDashboardSummaryVO getSummary() {
        DatahubDashboardSummaryVO vo = new DatahubDashboardSummaryVO();
        vo.setDatasourceCount(countDatasource());
        vo.setMetadataTableCount(countMetadataTable());
        vo.setMetadataColumnCount(countMetadataColumn());
        vo.setQualityRuleCount(countQualityRule());
        vo.setQualityResultCount(countQualityResult());
        vo.setSensitiveFieldCount(countSensitiveField());
        vo.setMaskRuleCount(countMaskRule());
        vo.setMaskResultCount(countMaskResult());
        vo.setApiPublishCount(countApiPublish());
        vo.setOnlineApiCount(countOnlineApi());
        return vo;
    }

    @Override
    public List<DatahubDatasourceTypeStatVO> getDatasourceTypeStats() {
        QueryWrapper<DatahubDatasource> wrapper = new QueryWrapper<>();
        wrapper.select("datasource_type AS statKey", "COUNT(*) AS statCount")
                .eq("deleted", 0)
                .groupBy("datasource_type");
        return datahubDatasourceMapper.selectMaps(wrapper).stream().map(map -> {
            String type = asString(map, "statKey");
            Long count = asLong(map, "statCount");
            DatahubDatasourceTypeStatVO vo = new DatahubDatasourceTypeStatVO();
            vo.setDatasourceType(type);
            vo.setTypeName(datasourceTypeName(type));
            vo.setCount(count);
            return vo;
        }).toList();
    }

    @Override
    public List<DatahubQualityResultStatVO> getQualityResultStats() {
        QueryWrapper<DatahubQualityResult> wrapper = new QueryWrapper<>();
        wrapper.select("check_status AS statKey", "COUNT(*) AS statCount")
                .eq("deleted", 0)
                .groupBy("check_status");
        return datahubQualityResultMapper.selectMaps(wrapper).stream().map(map -> {
            Integer status = asInteger(map, "statKey");
            Long count = asLong(map, "statCount");
            DatahubQualityResultStatVO vo = new DatahubQualityResultStatVO();
            vo.setCheckStatus(status);
            vo.setStatusName(qualityStatusName(status));
            vo.setCount(count);
            return vo;
        }).toList();
    }

    @Override
    public List<DatahubSensitiveTypeStatVO> getSensitiveTypeStats() {
        QueryWrapper<DatahubSensitiveField> wrapper = new QueryWrapper<>();
        wrapper.select("sensitive_type AS statKey", "COUNT(*) AS statCount")
                .eq("deleted", 0)
                .groupBy("sensitive_type");
        return datahubSensitiveFieldMapper.selectMaps(wrapper).stream().map(map -> {
            String type = asString(map, "statKey");
            Long count = asLong(map, "statCount");
            DatahubSensitiveTypeStatVO vo = new DatahubSensitiveTypeStatVO();
            vo.setSensitiveType(type);
            vo.setTypeName(sensitiveTypeName(type));
            vo.setCount(count);
            return vo;
        }).toList();
    }

    @Override
    public List<DatahubRecentQualityResultVO> getRecentQualityResults() {
        LambdaQueryWrapper<DatahubQualityResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DatahubQualityResult::getCheckTime)
                .orderByDesc(DatahubQualityResult::getId)
                .last("LIMIT 10");
        return datahubQualityResultMapper.selectList(wrapper).stream()
                .map(DatahubRecentQualityResultVO::fromEntity)
                .toList();
    }

    @Override
    public List<DatahubRecentApiPublishVO> getRecentApis() {
        LambdaQueryWrapper<DatahubApiPublish> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DatahubApiPublish::getCreateTime)
                .orderByDesc(DatahubApiPublish::getId)
                .last("LIMIT 10");
        return datahubApiPublishMapper.selectList(wrapper).stream()
                .map(DatahubRecentApiPublishVO::fromEntity)
                .toList();
    }

    private Long countDatasource() {
        return datahubDatasourceMapper.selectCount(new LambdaQueryWrapper<DatahubDatasource>().eq(DatahubDatasource::getDeleted, 0));
    }

    private Long countMetadataTable() {
        return datahubMetadataTableMapper.selectCount(new LambdaQueryWrapper<DatahubMetadataTable>().eq(DatahubMetadataTable::getDeleted, 0));
    }

    private Long countMetadataColumn() {
        return datahubMetadataColumnMapper.selectCount(new LambdaQueryWrapper<DatahubMetadataColumn>().eq(DatahubMetadataColumn::getDeleted, 0));
    }

    private Long countQualityRule() {
        return datahubQualityRuleMapper.selectCount(new LambdaQueryWrapper<DatahubQualityRule>().eq(DatahubQualityRule::getDeleted, 0));
    }

    private Long countQualityResult() {
        return datahubQualityResultMapper.selectCount(new LambdaQueryWrapper<DatahubQualityResult>().eq(DatahubQualityResult::getDeleted, 0));
    }

    private Long countSensitiveField() {
        return datahubSensitiveFieldMapper.selectCount(new LambdaQueryWrapper<DatahubSensitiveField>().eq(DatahubSensitiveField::getDeleted, 0));
    }

    private Long countMaskRule() {
        return datahubMaskRuleMapper.selectCount(new LambdaQueryWrapper<DatahubMaskRule>().eq(DatahubMaskRule::getDeleted, 0));
    }

    private Long countMaskResult() {
        return datahubMaskResultMapper.selectCount(new LambdaQueryWrapper<DatahubMaskResult>().eq(DatahubMaskResult::getDeleted, 0));
    }

    private Long countApiPublish() {
        return datahubApiPublishMapper.selectCount(new LambdaQueryWrapper<DatahubApiPublish>().eq(DatahubApiPublish::getDeleted, 0));
    }

    private Long countOnlineApi() {
        return datahubApiPublishMapper.selectCount(new LambdaQueryWrapper<DatahubApiPublish>()
                .eq(DatahubApiPublish::getDeleted, 0)
                .eq(DatahubApiPublish::getOnlineStatus, 1));
    }

    private String datasourceTypeName(String type) {
        if (type == null) {
            return "未知";
        }
        return switch (type) {
            case "MYSQL" -> "MySQL";
            case "ORACLE" -> "Oracle";
            case "POSTGRESQL" -> "PostgreSQL";
            case "API" -> "API";
            case "FILE" -> "文件";
            default -> type;
        };
    }

    private String qualityStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        return status == 0 ? "通过" : "不通过";
    }

    private String sensitiveTypeName(String type) {
        if (type == null) {
            return "未知";
        }
        return switch (type) {
            case "PHONE" -> "手机号";
            case "ID_CARD" -> "身份证";
            case "EMAIL" -> "邮箱";
            case "BANK_CARD" -> "银行卡";
            case "NAME" -> "姓名";
            case "ADDRESS" -> "地址";
            default -> type;
        };
    }

    private String asString(Map<String, Object> map, String key) {
        Object value = getMapValue(map, key);
        return value == null ? null : String.valueOf(value);
    }

    private Integer asInteger(Map<String, Object> map, String key) {
        Object value = getMapValue(map, key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private Long asLong(Map<String, Object> map, String key) {
        Object value = getMapValue(map, key);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private Object getMapValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        String lowerKey = key.toLowerCase();
        if (map.containsKey(lowerKey)) {
            return map.get(lowerKey);
        }
        String upperKey = key.toUpperCase();
        if (map.containsKey(upperKey)) {
            return map.get(upperKey);
        }
        return null;
    }
}
