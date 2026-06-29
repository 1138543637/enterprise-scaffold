package cn.sxu.enterprise.module.mine.vo;

public class MineMaintenanceTaskSummaryVO {

    private Long taskTotal;

    private Long pendingTotal;

    private Long plannedTotal;

    private Long processingTotal;

    private Long closedTotal;

    private Long highRiskTaskTotal;

    private Long urgentTotal;

    public Long getTaskTotal() {
        return taskTotal;
    }

    public void setTaskTotal(Long taskTotal) {
        this.taskTotal = taskTotal;
    }

    public Long getPendingTotal() {
        return pendingTotal;
    }

    public void setPendingTotal(Long pendingTotal) {
        this.pendingTotal = pendingTotal;
    }

    public Long getPlannedTotal() {
        return plannedTotal;
    }

    public void setPlannedTotal(Long plannedTotal) {
        this.plannedTotal = plannedTotal;
    }

    public Long getProcessingTotal() {
        return processingTotal;
    }

    public void setProcessingTotal(Long processingTotal) {
        this.processingTotal = processingTotal;
    }

    public Long getClosedTotal() {
        return closedTotal;
    }

    public void setClosedTotal(Long closedTotal) {
        this.closedTotal = closedTotal;
    }

    public Long getHighRiskTaskTotal() {
        return highRiskTaskTotal;
    }

    public void setHighRiskTaskTotal(Long highRiskTaskTotal) {
        this.highRiskTaskTotal = highRiskTaskTotal;
    }

    public Long getUrgentTotal() {
        return urgentTotal;
    }

    public void setUrgentTotal(Long urgentTotal) {
        this.urgentTotal = urgentTotal;
    }
}
