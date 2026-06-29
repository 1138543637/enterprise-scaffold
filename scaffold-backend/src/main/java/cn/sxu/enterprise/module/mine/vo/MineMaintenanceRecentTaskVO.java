package cn.sxu.enterprise.module.mine.vo;

import java.time.LocalDateTime;

public record MineMaintenanceRecentTaskVO(
        Long id,
        String taskCode,
        String deviceCode,
        String deviceName,
        String deviceType,
        String areaName,
        Integer healthScore,
        Integer riskLevel,
        String riskLevelName,
        Integer taskStatus,
        String taskStatusName,
        Integer priority,
        String priorityName,
        LocalDateTime createTime
) {
}
