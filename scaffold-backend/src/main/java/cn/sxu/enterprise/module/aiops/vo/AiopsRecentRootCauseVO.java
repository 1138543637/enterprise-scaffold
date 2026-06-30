package cn.sxu.enterprise.module.aiops.vo;

import java.time.LocalDateTime;

public record AiopsRecentRootCauseVO(
        Long id,
        String analysisCode,
        String eventCode,
        String resourceCode,
        String resourceName,
        String resourceType,
        String ipAddr,
        String rootCauseType,
        String rootCauseTypeName,
        Integer confidenceScore,
        LocalDateTime analysisTime
) {
}
