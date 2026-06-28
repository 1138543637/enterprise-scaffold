package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineSensorData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MineSensorDataVO {

    private Long id;

    private Long sensorId;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private Long deviceId;

    private String areaName;

    private String location;

    private BigDecimal dataValue;

    private String unit;

    private BigDecimal alarmThreshold;

    private Integer alarmFlag;

    private LocalDateTime collectTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static MineSensorDataVO fromEntity(MineSensorData entity) {
        if (entity == null) {
            return null;
        }

        MineSensorDataVO vo = new MineSensorDataVO();
        vo.setId(entity.getId());
        vo.setSensorId(entity.getSensorId());
        vo.setSensorCode(entity.getSensorCode());
        vo.setSensorName(entity.getSensorName());
        vo.setSensorType(entity.getSensorType());
        vo.setDeviceId(entity.getDeviceId());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setDataValue(entity.getDataValue());
        vo.setUnit(entity.getUnit());
        vo.setAlarmThreshold(entity.getAlarmThreshold());
        vo.setAlarmFlag(entity.getAlarmFlag());
        vo.setCollectTime(entity.getCollectTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineSensorDataVO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineSensorDataVO setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensorDataVO setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensorDataVO setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorDataVO setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineSensorDataVO setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensorDataVO setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineSensorDataVO setLocation(String location) {
        this.location = location;
        return this;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public MineSensorDataVO setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MineSensorDataVO setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getAlarmThreshold() {
        return alarmThreshold;
    }

    public MineSensorDataVO setAlarmThreshold(BigDecimal alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public MineSensorDataVO setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public MineSensorDataVO setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensorDataVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineSensorDataVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineSensorDataVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}