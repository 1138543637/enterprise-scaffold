package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineDevice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MineDevicePageVO {

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

    private LocalDateTime createTime;

    private String remark;

    public static MineDevicePageVO fromEntity(MineDevice entity) {
        MineDevicePageVO vo = new MineDevicePageVO();
        vo.setId(entity.getId());
        vo.setDeviceCode(entity.getDeviceCode());
        vo.setDeviceName(entity.getDeviceName());
        vo.setDeviceType(entity.getDeviceType());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setManufacturer(entity.getManufacturer());
        vo.setModel(entity.getModel());
        vo.setInstallDate(entity.getInstallDate());
        vo.setStatus(entity.getStatus());
        vo.setLastMaintenanceTime(entity.getLastMaintenanceTime());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineDevicePageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public MineDevicePageVO setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public MineDevicePageVO setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public MineDevicePageVO setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineDevicePageVO setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineDevicePageVO setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public MineDevicePageVO setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getModel() {
        return model;
    }

    public MineDevicePageVO setModel(String model) {
        this.model = model;
        return this;
    }

    public LocalDate getInstallDate() {
        return installDate;
    }

    public MineDevicePageVO setInstallDate(LocalDate installDate) {
        this.installDate = installDate;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineDevicePageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getLastMaintenanceTime() {
        return lastMaintenanceTime;
    }

    public MineDevicePageVO setLastMaintenanceTime(LocalDateTime lastMaintenanceTime) {
        this.lastMaintenanceTime = lastMaintenanceTime;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineDevicePageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineDevicePageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}