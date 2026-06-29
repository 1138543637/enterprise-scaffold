package cn.sxu.enterprise.module.mine.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MineRecentAlarmVO {

    private Long id;
    private String eventCode;
    private String sensorCode;
    private String sensorName;
    private String sensorType;
    private String areaName;
    private String location;
    private BigDecimal dataValue;
    private BigDecimal thresholdValue;
    private Integer alarmLevel;
    private String alarmLevelName;
    private Integer handleStatus;
    private String handleStatusName;
    private LocalDateTime alarmTime;

    public MineRecentAlarmVO() {
    }

    public MineRecentAlarmVO(Long id, String eventCode, String sensorCode, String sensorName,
                             String sensorType, String areaName, String location,
                             BigDecimal dataValue, BigDecimal thresholdValue,
                             Integer alarmLevel, String alarmLevelName,
                             Integer handleStatus, String handleStatusName,
                             LocalDateTime alarmTime) {
        this.id = id;
        this.eventCode = eventCode;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.areaName = areaName;
        this.location = location;
        this.dataValue = dataValue;
        this.thresholdValue = thresholdValue;
        this.alarmLevel = alarmLevel;
        this.alarmLevelName = alarmLevelName;
        this.handleStatus = handleStatus;
        this.handleStatusName = handleStatusName;
        this.alarmTime = alarmTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public void setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmLevelName() {
        return alarmLevelName;
    }

    public void setAlarmLevelName(String alarmLevelName) {
        this.alarmLevelName = alarmLevelName;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleStatusName() {
        return handleStatusName;
    }

    public void setHandleStatusName(String handleStatusName) {
        this.handleStatusName = handleStatusName;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }
}
