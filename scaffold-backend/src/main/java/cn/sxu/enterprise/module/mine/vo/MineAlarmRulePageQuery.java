package cn.sxu.enterprise.module.mine.vo;

public class MineAlarmRulePageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String ruleCode;

    private String ruleName;

    private String sensorType;

    private Integer alarmLevel;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public MineAlarmRulePageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public MineAlarmRulePageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmRulePageQuery setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public MineAlarmRulePageQuery setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmRulePageQuery setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmRulePageQuery setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmRulePageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
