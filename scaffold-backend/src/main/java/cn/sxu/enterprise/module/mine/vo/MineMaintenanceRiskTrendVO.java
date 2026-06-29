package cn.sxu.enterprise.module.mine.vo;

public record MineMaintenanceRiskTrendVO(
        String statDate,
        Long alarmTotal,
        Long workOrderTotal,
        Long maintenanceTaskTotal,
        Long highRiskTaskTotal
) {
}
