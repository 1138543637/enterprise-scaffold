package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("mine_sensor_data")
public class MineSensorData {

    @TableId(type = IdType.AUTO)
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

    public MineSensorData setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineSensorData setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensorData setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensorData setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorData setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineSensorData setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensorData setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineSensorData setLocation(String location) {
        this.location = location;
        return this;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public MineSensorData setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MineSensorData setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getAlarmThreshold() {
        return alarmThreshold;
    }

    public MineSensorData setAlarmThreshold(BigDecimal alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public MineSensorData setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public MineSensorData setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensorData setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineSensorData setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineSensorData setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineSensorData setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineSensorData setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineSensorData setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineSensorData setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}