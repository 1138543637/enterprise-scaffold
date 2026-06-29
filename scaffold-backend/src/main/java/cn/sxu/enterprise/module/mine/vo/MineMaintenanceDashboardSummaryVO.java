package cn.sxu.enterprise.module.mine.vo;

public record MineMaintenanceDashboardSummaryVO(
        Long taskTotal,
        Long unclosedTaskTotal,
        Long pendingTotal,
        Long plannedTotal,
        Long processingTotal,
        Long closedTotal,
        Long highRiskTaskTotal,
        Long urgentTotal,
        Long todayNewTaskTotal,
        Long alarmTotal7d,
        Long workOrderTotal7d
) {
}
