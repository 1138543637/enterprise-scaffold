package cn.sxu.enterprise.module.mine.vo;

public class MineDashboardSummaryVO {

    private Long deviceTotal;
    private Long sensorTotal;
    private Long sensorDataTotal;
    private Long alarmRuleTotal;
    private Long alarmEventTotal;
    private Long unhandledAlarmTotal;
    private Long workOrderTotal;
    private Long pendingWorkOrderTotal;
    private Long closedWorkOrderTotal;

    public MineDashboardSummaryVO() {
    }

    public MineDashboardSummaryVO(Long deviceTotal, Long sensorTotal, Long sensorDataTotal,
                                  Long alarmRuleTotal, Long alarmEventTotal, Long unhandledAlarmTotal,
                                  Long workOrderTotal, Long pendingWorkOrderTotal, Long closedWorkOrderTotal) {
        this.deviceTotal = deviceTotal;
        this.sensorTotal = sensorTotal;
        this.sensorDataTotal = sensorDataTotal;
        this.alarmRuleTotal = alarmRuleTotal;
        this.alarmEventTotal = alarmEventTotal;
        this.unhandledAlarmTotal = unhandledAlarmTotal;
        this.workOrderTotal = workOrderTotal;
        this.pendingWorkOrderTotal = pendingWorkOrderTotal;
        this.closedWorkOrderTotal = closedWorkOrderTotal;
    }

    public Long getDeviceTotal() {
        return deviceTotal;
    }

    public void setDeviceTotal(Long deviceTotal) {
        this.deviceTotal = deviceTotal;
    }

    public Long getSensorTotal() {
        return sensorTotal;
    }

    public void setSensorTotal(Long sensorTotal) {
        this.sensorTotal = sensorTotal;
    }

    public Long getSensorDataTotal() {
        return sensorDataTotal;
    }

    public void setSensorDataTotal(Long sensorDataTotal) {
        this.sensorDataTotal = sensorDataTotal;
    }

    public Long getAlarmRuleTotal() {
        return alarmRuleTotal;
    }

    public void setAlarmRuleTotal(Long alarmRuleTotal) {
        this.alarmRuleTotal = alarmRuleTotal;
    }

    public Long getAlarmEventTotal() {
        return alarmEventTotal;
    }

    public void setAlarmEventTotal(Long alarmEventTotal) {
        this.alarmEventTotal = alarmEventTotal;
    }

    public Long getUnhandledAlarmTotal() {
        return unhandledAlarmTotal;
    }

    public void setUnhandledAlarmTotal(Long unhandledAlarmTotal) {
        this.unhandledAlarmTotal = unhandledAlarmTotal;
    }

    public Long getWorkOrderTotal() {
        return workOrderTotal;
    }

    public void setWorkOrderTotal(Long workOrderTotal) {
        this.workOrderTotal = workOrderTotal;
    }

    public Long getPendingWorkOrderTotal() {
        return pendingWorkOrderTotal;
    }

    public void setPendingWorkOrderTotal(Long pendingWorkOrderTotal) {
        this.pendingWorkOrderTotal = pendingWorkOrderTotal;
    }

    public Long getClosedWorkOrderTotal() {
        return closedWorkOrderTotal;
    }

    public void setClosedWorkOrderTotal(Long closedWorkOrderTotal) {
        this.closedWorkOrderTotal = closedWorkOrderTotal;
    }
}
