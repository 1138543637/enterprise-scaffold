package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineSensor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MineSensorPageVO {

    private Long id;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private Long deviceId;

    private String areaName;

    private String location;

    private String unit;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private BigDecimal alarmThreshold;

    private Integer status;

    private LocalDateTime lastReportTime;

    private LocalDateTime createTime;

    private String remark;

    public static MineSensorPageVO fromEntity(MineSensor entity) {
        MineSensorPageVO vo = new MineSensorPageVO();
        vo.setId(entity.getId());
        vo.setSensorCode(entity.getSensorCode());
        vo.setSensorName(entity.getSensorName());
        vo.setSensorType(entity.getSensorType());
        vo.setDeviceId(entity.getDeviceId());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setUnit(entity.getUnit());
        vo.setMinValue(entity.getMinValue());
        vo.setMaxValue(entity.getMaxValue());
        vo.setAlarmThreshold(entity.getAlarmThreshold());
        vo.setStatus(entity.getStatus());
        vo.setLastReportTime(entity.getLastReportTime());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineSensorPageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensorPageVO setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensorPageVO setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorPageVO setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineSensorPageVO setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensorPageVO setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineSensorPageVO setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MineSensorPageVO setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public MineSensorPageVO setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
        return this;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public MineSensorPageVO setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public BigDecimal getAlarmThreshold() {
        return alarmThreshold;
    }

    public MineSensorPageVO setAlarmThreshold(BigDecimal alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensorPageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getLastReportTime() {
        return lastReportTime;
    }

    public MineSensorPageVO setLastReportTime(LocalDateTime lastReportTime) {
        this.lastReportTime = lastReportTime;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineSensorPageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineSensorPageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}