package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("mine_alarm_event")
public class MineAlarmEvent {

    @TableId(type = IdType.AUTO)
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

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;

    public Long getId() {
        return id;
    }

    public MineAlarmEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public MineAlarmEvent setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public MineAlarmEvent setRuleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmEvent setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public MineAlarmEvent setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public Long getSensorDataId() {
        return sensorDataId;
    }

    public MineAlarmEvent setSensorDataId(Long sensorDataId) {
        this.sensorDataId = sensorDataId;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineAlarmEvent setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineAlarmEvent setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineAlarmEvent setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmEvent setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineAlarmEvent setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineAlarmEvent setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineAlarmEvent setLocation(String location) {
        this.location = location;
        return this;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public MineAlarmEvent setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public MineAlarmEvent setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public MineAlarmEvent setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmEvent setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public MineAlarmEvent setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
        return this;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public MineAlarmEvent setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
        return this;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public MineAlarmEvent setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
        return this;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public MineAlarmEvent setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmEvent setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineAlarmEvent setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineAlarmEvent setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineAlarmEvent setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineAlarmEvent setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineAlarmEvent setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineAlarmEvent setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
