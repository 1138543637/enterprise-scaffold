package cn.sxu.enterprise.module.mine.vo;

public class MineAlarmEventPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String eventCode;

    private String ruleCode;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private String areaName;

    private Integer alarmLevel;

    private Integer handleStatus;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public MineAlarmEventPageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public MineAlarmEventPageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public MineAlarmEventPageQuery setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmEventPageQuery setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineAlarmEventPageQuery setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineAlarmEventPageQuery setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmEventPageQuery setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineAlarmEventPageQuery setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmEventPageQuery setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public MineAlarmEventPageQuery setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmEventPageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
