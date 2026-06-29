package cn.sxu.enterprise.module.mine.vo;

import java.time.LocalDateTime;

public class MineRecentWorkOrderVO {

    private Long id;
    private String workOrderCode;
    private String eventCode;
    private String sensorCode;
    private String sensorName;
    private String sensorType;
    private String areaName;
    private String location;
    private Integer alarmLevel;
    private String alarmLevelName;
    private Integer orderStatus;
    private String orderStatusName;
    private LocalDateTime createTime;

    public MineRecentWorkOrderVO() {
    }

    public MineRecentWorkOrderVO(Long id, String workOrderCode, String eventCode,
                                 String sensorCode, String sensorName, String sensorType,
                                 String areaName, String location,
                                 Integer alarmLevel, String alarmLevelName,
                                 Integer orderStatus, String orderStatusName,
                                 LocalDateTime createTime) {
        this.id = id;
        this.workOrderCode = workOrderCode;
        this.eventCode = eventCode;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.areaName = areaName;
        this.location = location;
        this.alarmLevel = alarmLevel;
        this.alarmLevelName = alarmLevelName;
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatusName;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
