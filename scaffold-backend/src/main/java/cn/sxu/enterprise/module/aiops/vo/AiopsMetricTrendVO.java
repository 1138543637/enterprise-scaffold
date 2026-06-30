package cn.sxu.enterprise.module.aiops.vo;

import java.math.BigDecimal;

public record AiopsMetricTrendVO(
        String statDate,
        Long metricTotal,
        Long alertMetricTotal,
        BigDecimal avgMetricValue
) {
}
