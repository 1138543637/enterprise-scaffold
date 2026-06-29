package cn.sxu.enterprise.module.mine.vo;

public class MineDeviceHealthSummaryVO {

    private Long deviceTotal;

    private Long healthyTotal;

    private Long attentionTotal;

    private Long riskTotal;

    private Long highRiskTotal;

    private Integer averageHealthScore;

    private Long severeUnhandledAlarmTotal;

    private Long unclosedWorkOrderTotal;

    public Long getDeviceTotal() {
        return deviceTotal;
    }

    public void setDeviceTotal(Long deviceTotal) {
        this.deviceTotal = deviceTotal;
    }

    public Long getHealthyTotal() {
        return healthyTotal;
    }

    public void setHealthyTotal(Long healthyTotal) {
        this.healthyTotal = healthyTotal;
    }

    public Long getAttentionTotal() {
        return attentionTotal;
    }

    public void setAttentionTotal(Long attentionTotal) {
        this.attentionTotal = attentionTotal;
    }

    public Long getRiskTotal() {
        return riskTotal;
    }

    public void setRiskTotal(Long riskTotal) {
        this.riskTotal = riskTotal;
    }

    public Long getHighRiskTotal() {
        return highRiskTotal;
    }

    public void setHighRiskTotal(Long highRiskTotal) {
        this.highRiskTotal = highRiskTotal;
    }

    public Integer getAverageHealthScore() {
        return averageHealthScore;
    }

    public void setAverageHealthScore(Integer averageHealthScore) {
        this.averageHealthScore = averageHealthScore;
    }

    public Long getSevereUnhandledAlarmTotal() {
        return severeUnhandledAlarmTotal;
    }

    public void setSevereUnhandledAlarmTotal(Long severeUnhandledAlarmTotal) {
        this.severeUnhandledAlarmTotal = severeUnhandledAlarmTotal;
    }

    public Long getUnclosedWorkOrderTotal() {
        return unclosedWorkOrderTotal;
    }

    public void setUnclosedWorkOrderTotal(Long unclosedWorkOrderTotal) {
        this.unclosedWorkOrderTotal = unclosedWorkOrderTotal;
    }
}
