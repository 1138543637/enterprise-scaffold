package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MineAlarmEventPageVO {

    private Long id;

    private String eventCode;

    private Long ruleId;

    private String ruleCode;

    private String ruleName;

    private Long sensorDataId;

    private Long sensorId;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private Long deviceId;

    private String areaName;

    private String location;

    private BigDecimal dataValue;

    private BigDecimal thresholdValue;

    private String compareOperator;

    private Integer alarmLevel;

    private String alarmTitle;

    private String alarmContent;

    private LocalDateTime alarmTime;

    private Integer handleStatus;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static MineAlarmEventPageVO fromEntity(MineAlarmEvent entity) {
        if (entity == null) {
            return null;
        }

        MineAlarmEventPageVO vo = new MineAlarmEventPageVO();
        vo.setId(entity.getId());
        vo.setEventCode(entity.getEventCode());
        vo.setRuleId(entity.getRuleId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setSensorDataId(entity.getSensorDataId());
        vo.setSensorId(entity.getSensorId());
        vo.setSensorCode(entity.getSensorCode());
        vo.setSensorName(entity.getSensorName());
        vo.setSensorType(entity.getSensorType());
        vo.setDeviceId(entity.getDeviceId());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setDataValue(entity.getDataValue());
        vo.setThresholdValue(entity.getThresholdValue());
        vo.setCompareOperator(entity.getCompareOperator());
        vo.setAlarmLevel(entity.getAlarmLevel());
        vo.setAlarmTitle(entity.getAlarmTitle());
        vo.setAlarmContent(entity.getAlarmContent());
        vo.setAlarmTime(entity.getAlarmTime());
        vo.setHandleStatus(entity.getHandleStatus());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineAlarmEventPageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public MineAlarmEventPageVO setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public MineAlarmEventPageVO setRuleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmEventPageVO setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public MineAlarmEventPageVO setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public Long getSensorDataId() {
        return sensorDataId;
    }

    public MineAlarmEventPageVO setSensorDataId(Long sensorDataId) {
        this.sensorDataId = sensorDataId;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineAlarmEventPageVO setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineAlarmEventPageVO setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineAlarmEventPageVO setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmEventPageVO setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineAlarmEventPageVO setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineAlarmEventPageVO setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineAlarmEventPageVO setLocation(String location) {
        this.location = location;
        return this;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public MineAlarmEventPageVO setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public MineAlarmEventPageVO setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public MineAlarmEventPageVO setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmEventPageVO setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public MineAlarmEventPageVO setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
        return this;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public MineAlarmEventPageVO setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
        return this;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public MineAlarmEventPageVO setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
        return this;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public MineAlarmEventPageVO setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmEventPageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineAlarmEventPageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineAlarmEventPageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
