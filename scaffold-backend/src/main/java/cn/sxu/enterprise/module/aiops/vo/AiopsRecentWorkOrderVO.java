package cn.sxu.enterprise.module.aiops.vo;

import java.time.LocalDateTime;

public record AiopsRecentWorkOrderVO(
        Long id,
        String workOrderCode,
        String eventCode,
        String resourceCode,
        String resourceName,
        String resourceType,
        String ipAddr,
        String metricType,
        Integer alertLevel,
        String alertLevelName,
        Integer orderStatus,
        String orderStatusName,
        LocalDateTime createTime
) {
}
