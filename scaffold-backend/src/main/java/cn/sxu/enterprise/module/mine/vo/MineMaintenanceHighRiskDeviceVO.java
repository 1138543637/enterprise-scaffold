package cn.sxu.enterprise.module.mine.vo;

import java.time.LocalDateTime;

public record MineMaintenanceHighRiskDeviceVO(
        Long id,
        String taskCode,
        Long deviceId,
        String deviceCode,
        String deviceName,
        String deviceType,
        String areaName,
        String location,
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
