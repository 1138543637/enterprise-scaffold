package cn.sxu.enterprise.module.aiops.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AiopsRecentAlertVO(
        Long id,
        String eventCode,
        String resourceCode,
        String resourceName,
        String resourceType,
        String ipAddr,
        String metricType,
        BigDecimal metricValue,
        BigDecimal thresholdValue,
        Integer alertLevel,
        String alertLevelName,
        Integer handleStatus,
        String handleStatusName,
        LocalDateTime alertTime
) {
}
