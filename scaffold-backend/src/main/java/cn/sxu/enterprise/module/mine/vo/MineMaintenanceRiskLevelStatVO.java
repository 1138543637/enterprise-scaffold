package cn.sxu.enterprise.module.mine.vo;

public record MineMaintenanceRiskLevelStatVO(
        Integer riskLevel,
        String riskLevelName,
        Long total
) {
}
