package cn.sxu.enterprise.module.mine.vo;

import java.time.LocalDateTime;

public class MineDeviceHealthVO {

    private Long id;

    private String deviceCode;

    private String deviceName;

    private String deviceType;

    private String areaName;

    private String location;

    private Integer healthScore;

    private Integer riskLevel;

    private String riskLevelName;

    private Long alarmCount24h;

    private Long severeUnhandledAlarmCount;

    private Long unclosedWorkOrderCount;

    private Long sensorTotal;

    private Long offlineSensorCount;

    private LocalDateTime lastReportTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevelName() {
        return riskLevelName;
    }

    public void setRiskLevelName(String riskLevelName) {
        this.riskLevelName = riskLevelName;
    }

    public Long getAlarmCount24h() {
        return alarmCount24h;
    }

    public void setAlarmCount24h(Long alarmCount24h) {
        this.alarmCount24h = alarmCount24h;
    }

    public Long getSevereUnhandledAlarmCount() {
        return severeUnhandledAlarmCount;
    }

    public void setSevereUnhandledAlarmCount(Long severeUnhandledAlarmCount) {
        this.severeUnhandledAlarmCount = severeUnhandledAlarmCount;
    }

    public Long getUnclosedWorkOrderCount() {
        return unclosedWorkOrderCount;
    }

    public void setUnclosedWorkOrderCount(Long unclosedWorkOrderCount) {
        this.unclosedWorkOrderCount = unclosedWorkOrderCount;
    }

    public Long getSensorTotal() {
        return sensorTotal;
    }

    public void setSensorTotal(Long sensorTotal) {
        this.sensorTotal = sensorTotal;
    }

    public Long getOfflineSensorCount() {
        return offlineSensorCount;
    }

    public void setOfflineSensorCount(Long offlineSensorCount) {
        this.offlineSensorCount = offlineSensorCount;
    }

    public LocalDateTime getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(LocalDateTime lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
