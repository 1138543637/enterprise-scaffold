package cn.sxu.enterprise.module.aiops.vo;

public record AiopsDashboardSummaryVO(
        Long resourceTotal,
        Long abnormalResourceTotal,
        Long metricDataTotal,
        Long alertMetricTotal,
        Long alertRuleTotal,
        Long alertEventTotal,
        Long unhandledAlertTotal,
        Long workOrderTotal,
        Long pendingWorkOrderTotal,
        Long rootCauseTotal,
        Long highConfidenceRootCauseTotal
) {
}
