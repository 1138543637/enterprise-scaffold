package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.module.aiops.entity.AiopsAlertEvent;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertRule;
import cn.sxu.enterprise.module.aiops.entity.AiopsMetricData;
import cn.sxu.enterprise.module.aiops.entity.AiopsResource;
import cn.sxu.enterprise.module.aiops.entity.AiopsRootCauseRecord;
import cn.sxu.enterprise.module.aiops.entity.AiopsWorkOrder;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertEventMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertRuleMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsMetricDataMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsResourceMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsRootCauseRecordMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsWorkOrderMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsDashboardService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertLevelStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsDashboardSummaryVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricTrendVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentAlertVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentRootCauseVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentWorkOrderVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourceTypeStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderStatusStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiopsDashboardServiceImpl implements AiopsDashboardService {

    private final AiopsResourceMapper aiopsResourceMapper;
    private final AiopsMetricDataMapper aiopsMetricDataMapper;
    private final AiopsAlertRuleMapper aiopsAlertRuleMapper;
    private final AiopsAlertEventMapper aiopsAlertEventMapper;
    private final AiopsWorkOrderMapper aiopsWorkOrderMapper;
    private final AiopsRootCauseRecordMapper aiopsRootCauseRecordMapper;

    public AiopsDashboardServiceImpl(AiopsResourceMapper aiopsResourceMapper,
                                     AiopsMetricDataMapper aiopsMetricDataMapper,
                                     AiopsAlertRuleMapper aiopsAlertRuleMapper,
                                     AiopsAlertEventMapper aiopsAlertEventMapper,
                                     AiopsWorkOrderMapper aiopsWorkOrderMapper,
                                     AiopsRootCauseRecordMapper aiopsRootCauseRecordMapper) {
        this.aiopsResourceMapper = aiopsResourceMapper;
        this.aiopsMetricDataMapper = aiopsMetricDataMapper;
        this.aiopsAlertRuleMapper = aiopsAlertRuleMapper;
        this.aiopsAlertEventMapper = aiopsAlertEventMapper;
        this.aiopsWorkOrderMapper = aiopsWorkOrderMapper;
        this.aiopsRootCauseRecordMapper = aiopsRootCauseRecordMapper;
    }

    @Override
    public AiopsDashboardSummaryVO getSummary() {
        Long resourceTotal = aiopsResourceMapper.selectCount(new LambdaQueryWrapper<AiopsResource>());
        Long abnormalResourceTotal = aiopsResourceMapper.selectCount(
                new LambdaQueryWrapper<AiopsResource>().eq(AiopsResource::getStatus, 2)
        );

        Long metricDataTotal = aiopsMetricDataMapper.selectCount(new LambdaQueryWrapper<AiopsMetricData>());
        Long alertMetricTotal = aiopsMetricDataMapper.selectCount(
                new LambdaQueryWrapper<AiopsMetricData>().eq(AiopsMetricData::getAlarmFlag, 1)
        );

        Long alertRuleTotal = aiopsAlertRuleMapper.selectCount(new LambdaQueryWrapper<AiopsAlertRule>());
        Long alertEventTotal = aiopsAlertEventMapper.selectCount(new LambdaQueryWrapper<AiopsAlertEvent>());
        Long unhandledAlertTotal = aiopsAlertEventMapper.selectCount(
                new LambdaQueryWrapper<AiopsAlertEvent>().eq(AiopsAlertEvent::getHandleStatus, 0)
        );

        Long workOrderTotal = aiopsWorkOrderMapper.selectCount(new LambdaQueryWrapper<AiopsWorkOrder>());
        Long pendingWorkOrderTotal = aiopsWorkOrderMapper.selectCount(
                new LambdaQueryWrapper<AiopsWorkOrder>().eq(AiopsWorkOrder::getOrderStatus, 0)
        );

        Long rootCauseTotal = aiopsRootCauseRecordMapper.selectCount(new LambdaQueryWrapper<AiopsRootCauseRecord>());
        Long highConfidenceRootCauseTotal = aiopsRootCauseRecordMapper.selectCount(
                new LambdaQueryWrapper<AiopsRootCauseRecord>().ge(AiopsRootCauseRecord::getConfidenceScore, 80)
        );

        return new AiopsDashboardSummaryVO(
                resourceTotal,
                abnormalResourceTotal,
                metricDataTotal,
                alertMetricTotal,
                alertRuleTotal,
                alertEventTotal,
                unhandledAlertTotal,
                workOrderTotal,
                pendingWorkOrderTotal,
                rootCauseTotal,
                highConfidenceRootCauseTotal
        );
    }

    @Override
    public List<AiopsResourceTypeStatVO> getResourceTypeStats() {
        QueryWrapper<AiopsResource> wrapper = new QueryWrapper<>();
        wrapper.select("resource_type AS resourceType", "COUNT(*) AS total")
                .eq("deleted", 0)
                .groupBy("resource_type")
                .orderByAsc("resource_type");

        return aiopsResourceMapper.selectMaps(wrapper)
                .stream()
                .map(item -> {
                    String resourceType = stringValue(item, "resourceType", "resource_type");
                    Long total = longValue(item, "total");
                    return new AiopsResourceTypeStatVO(resourceType, resourceTypeName(resourceType), total);
                })
                .toList();
    }

    @Override
    public List<AiopsAlertLevelStatVO> getAlertLevelStats() {
        QueryWrapper<AiopsAlertEvent> wrapper = new QueryWrapper<>();
        wrapper.select("alert_level AS alertLevel", "COUNT(*) AS total")
                .eq("deleted", 0)
                .groupBy("alert_level")
                .orderByAsc("alert_level");

        return aiopsAlertEventMapper.selectMaps(wrapper)
                .stream()
                .map(item -> {
                    Integer alertLevel = integerValue(item, "alertLevel", "alert_level");
                    Long total = longValue(item, "total");
                    return new AiopsAlertLevelStatVO(alertLevel, alertLevelName(alertLevel), total);
                })
                .toList();
    }

    @Override
    public List<AiopsWorkOrderStatusStatVO> getWorkOrderStatusStats() {
        QueryWrapper<AiopsWorkOrder> wrapper = new QueryWrapper<>();
        wrapper.select("order_status AS orderStatus", "COUNT(*) AS total")
                .eq("deleted", 0)
                .groupBy("order_status")
                .orderByAsc("order_status");

        return aiopsWorkOrderMapper.selectMaps(wrapper)
                .stream()
                .map(item -> {
                    Integer orderStatus = integerValue(item, "orderStatus", "order_status");
                    Long total = longValue(item, "total");
                    return new AiopsWorkOrderStatusStatVO(orderStatus, orderStatusName(orderStatus), total);
                })
                .toList();
    }

    @Override
    public List<AiopsMetricTrendVO> getMetricTrend() {
        LocalDate startDate = LocalDate.now().minusDays(6);

        QueryWrapper<AiopsMetricData> wrapper = new QueryWrapper<>();
        wrapper.select(
                        "DATE(collect_time) AS statDate",
                        "COUNT(*) AS metricTotal",
                        "SUM(CASE WHEN alarm_flag = 1 THEN 1 ELSE 0 END) AS alertMetricTotal",
                        "ROUND(AVG(metric_value), 2) AS avgMetricValue"
                )
                .eq("deleted", 0)
                .ge("collect_time", startDate.atStartOfDay())
                .groupBy("DATE(collect_time)")
                .orderByAsc("statDate");

        Map<String, AiopsMetricTrendVO> trendMap = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            String date = startDate.plusDays(i).toString();
            trendMap.put(date, new AiopsMetricTrendVO(date, 0L, 0L, BigDecimal.ZERO));
        }

        for (Map<String, Object> item : aiopsMetricDataMapper.selectMaps(wrapper)) {
            String statDate = stringValue(item, "statDate", "stat_date");
            if (statDate == null || statDate.isBlank()) {
                continue;
            }

            Long metricTotal = longValue(item, "metricTotal", "metric_total");
            Long alertMetricTotal = longValue(item, "alertMetricTotal", "alert_metric_total");
            BigDecimal avgMetricValue = bigDecimalValue(item, "avgMetricValue", "avg_metric_value");

            trendMap.put(statDate, new AiopsMetricTrendVO(
                    statDate,
                    metricTotal,
                    alertMetricTotal,
                    avgMetricValue
            ));
        }

        return new ArrayList<>(trendMap.values());
    }

    @Override
    public List<AiopsRecentAlertVO> getRecentAlerts() {
        Page<AiopsAlertEvent> page = new Page<>(1, 10);
        LambdaQueryWrapper<AiopsAlertEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AiopsAlertEvent::getAlertTime)
                .orderByDesc(AiopsAlertEvent::getId);

        return aiopsAlertEventMapper.selectPage(page, wrapper)
                .getRecords()
                .stream()
                .map(item -> new AiopsRecentAlertVO(
                        item.getId(),
                        item.getEventCode(),
                        item.getResourceCode(),
                        item.getResourceName(),
                        item.getResourceType(),
                        item.getIpAddr(),
                        item.getMetricType(),
                        item.getMetricValue(),
                        item.getThresholdValue(),
                        item.getAlertLevel(),
                        alertLevelName(item.getAlertLevel()),
                        item.getHandleStatus(),
                        handleStatusName(item.getHandleStatus()),
                        item.getAlertTime()
                ))
                .toList();
    }

    @Override
    public List<AiopsRecentWorkOrderVO> getRecentWorkOrders() {
        Page<AiopsWorkOrder> page = new Page<>(1, 10);
        LambdaQueryWrapper<AiopsWorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AiopsWorkOrder::getCreateTime)
                .orderByDesc(AiopsWorkOrder::getId);

        return aiopsWorkOrderMapper.selectPage(page, wrapper)
                .getRecords()
                .stream()
                .map(item -> new AiopsRecentWorkOrderVO(
                        item.getId(),
                        item.getWorkOrderCode(),
                        item.getEventCode(),
                        item.getResourceCode(),
                        item.getResourceName(),
                        item.getResourceType(),
                        item.getIpAddr(),
                        item.getMetricType(),
                        item.getAlertLevel(),
                        alertLevelName(item.getAlertLevel()),
                        item.getOrderStatus(),
                        orderStatusName(item.getOrderStatus()),
                        item.getCreateTime()
                ))
                .toList();
    }

    @Override
    public List<AiopsRecentRootCauseVO> getRecentRootCauses() {
        Page<AiopsRootCauseRecord> page = new Page<>(1, 10);
        LambdaQueryWrapper<AiopsRootCauseRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AiopsRootCauseRecord::getAnalysisTime)
                .orderByDesc(AiopsRootCauseRecord::getId);

        return aiopsRootCauseRecordMapper.selectPage(page, wrapper)
                .getRecords()
                .stream()
                .map(item -> new AiopsRecentRootCauseVO(
                        item.getId(),
                        item.getAnalysisCode(),
                        item.getEventCode(),
                        item.getResourceCode(),
                        item.getResourceName(),
                        item.getResourceType(),
                        item.getIpAddr(),
                        item.getRootCauseType(),
                        rootCauseTypeName(item.getRootCauseType()),
                        item.getConfidenceScore(),
                        item.getAnalysisTime()
                ))
                .toList();
    }

    private String resourceTypeName(String resourceType) {
        if ("SERVER".equals(resourceType)) {
            return "服务器";
        }
        if ("DATABASE".equals(resourceType)) {
            return "数据库";
        }
        if ("MIDDLEWARE".equals(resourceType)) {
            return "中间件";
        }
        if ("NETWORK".equals(resourceType)) {
            return "网络设备";
        }
        return "未知资源";
    }

    private String alertLevelName(Integer alertLevel) {
        if (alertLevel == null) {
            return "未知";
        }
        return switch (alertLevel) {
            case 1 -> "一般";
            case 2 -> "重要";
            case 3 -> "严重";
            default -> "未知";
        };
    }

    private String handleStatusName(Integer handleStatus) {
        if (handleStatus == null) {
            return "未知";
        }
        return switch (handleStatus) {
            case 0 -> "未处理";
            case 1 -> "已派单";
            case 2 -> "已关闭";
            default -> "未知";
        };
    }

    private String orderStatusName(Integer orderStatus) {
        if (orderStatus == null) {
            return "未知";
        }
        return switch (orderStatus) {
            case 0 -> "待处理";
            case 1 -> "处理中";
            case 2 -> "已处理";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    private String rootCauseTypeName(String rootCauseType) {
        if ("CPU_HIGH".equals(rootCauseType)) {
            return "CPU 使用率过高";
        }
        if ("MEMORY_HIGH".equals(rootCauseType)) {
            return "内存使用率过高";
        }
        if ("DISK_FULL".equals(rootCauseType)) {
            return "磁盘空间不足";
        }
        if ("NETWORK_ABNORMAL".equals(rootCauseType)) {
            return "网络异常";
        }
        if ("DATABASE_SLOW".equals(rootCauseType)) {
            return "数据库响应慢";
        }
        if ("UNKNOWN".equals(rootCauseType)) {
            return "未知原因";
        }
        return "未知原因";
    }

    private String stringValue(Map<String, Object> map, String... keys) {
        Object value = objectValue(map, keys);
        return value == null ? null : String.valueOf(value);
    }

    private Integer integerValue(Map<String, Object> map, String... keys) {
        Object value = objectValue(map, keys);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private Long longValue(Map<String, Object> map, String... keys) {
        Object value = objectValue(map, keys);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private BigDecimal bigDecimalValue(Map<String, Object> map, String... keys) {
        Object value = objectValue(map, keys);
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal.setScale(2, RoundingMode.HALF_UP);
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }
        return new BigDecimal(String.valueOf(value)).setScale(2, RoundingMode.HALF_UP);
    }

    private Object objectValue(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
}
