package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("mine_device")
public class MineDevice {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String deviceCode;

    private String deviceName;

    private String deviceType;

    private String areaName;

    private String location;

    private String manufacturer;

    private String model;

    private LocalDate installDate;

    private Integer status;

    private LocalDateTime lastMaintenanceTime;

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

    public MineDevice setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public MineDevice setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public MineDevice setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public MineDevice setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineDevice setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineDevice setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public MineDevice setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getModel() {
        return model;
    }

    public MineDevice setModel(String model) {
        this.model = model;
        return this;
    }

    public LocalDate getInstallDate() {
        return installDate;
    }

    public MineDevice setInstallDate(LocalDate installDate) {
        this.installDate = installDate;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineDevice setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getLastMaintenanceTime() {
        return lastMaintenanceTime;
    }

    public MineDevice setLastMaintenanceTime(LocalDateTime lastMaintenanceTime) {
        this.lastMaintenanceTime = lastMaintenanceTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineDevice setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineDevice setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineDevice setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineDevice setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineDevice setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineDevice setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}