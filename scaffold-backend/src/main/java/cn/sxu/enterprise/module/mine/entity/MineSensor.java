package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("mine_sensor")
public class MineSensor {

    @TableId(type = IdType.AUTO)
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

    public MineSensor setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensor setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensor setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensor setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineSensor setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensor setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineSensor setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MineSensor setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public MineSensor setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
        return this;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public MineSensor setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public BigDecimal getAlarmThreshold() {
        return alarmThreshold;
    }

    public MineSensor setAlarmThreshold(BigDecimal alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensor setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getLastReportTime() {
        return lastReportTime;
    }

    public MineSensor setLastReportTime(LocalDateTime lastReportTime) {
        this.lastReportTime = lastReportTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineSensor setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineSensor setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineSensor setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineSensor setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineSensor setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineSensor setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}