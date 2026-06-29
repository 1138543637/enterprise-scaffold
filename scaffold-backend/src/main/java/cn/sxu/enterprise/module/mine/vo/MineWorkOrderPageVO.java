package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;

import java.time.LocalDateTime;

public class MineWorkOrderPageVO {

    private Long id;

    private String workOrderCode;

    private Long alarmEventId;

    private String eventCode;

    private Integer alarmLevel;

    private String workOrderTitle;

    private String workOrderContent;

    private Long deviceId;

    private Long sensorId;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private String areaName;

    private String location;

    private Integer orderStatus;

    private Long handlerUserId;

    private String handlerUsername;

    private LocalDateTime handleTime;

    private String handleResult;

    private Long closeUserId;

    private String closeUsername;

    private LocalDateTime closeTime;

    private String closeResult;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static MineWorkOrderPageVO fromEntity(MineWorkOrder entity) {
        if (entity == null) {
            return null;
        }

        MineWorkOrderPageVO vo = new MineWorkOrderPageVO();
        vo.setId(entity.getId());
        vo.setWorkOrderCode(entity.getWorkOrderCode());
        vo.setAlarmEventId(entity.getAlarmEventId());
        vo.setEventCode(entity.getEventCode());
        vo.setAlarmLevel(entity.getAlarmLevel());
        vo.setWorkOrderTitle(entity.getWorkOrderTitle());
        vo.setWorkOrderContent(entity.getWorkOrderContent());
        vo.setDeviceId(entity.getDeviceId());
        vo.setSensorId(entity.getSensorId());
        vo.setSensorCode(entity.getSensorCode());
        vo.setSensorName(entity.getSensorName());
        vo.setSensorType(entity.getSensorType());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setOrderStatus(entity.getOrderStatus());
        vo.setHandlerUserId(entity.getHandlerUserId());
        vo.setHandlerUsername(entity.getHandlerUsername());
        vo.setHandleTime(entity.getHandleTime());
        vo.setHandleResult(entity.getHandleResult());
        vo.setCloseUserId(entity.getCloseUserId());
        vo.setCloseUsername(entity.getCloseUsername());
        vo.setCloseTime(entity.getCloseTime());
        vo.setCloseResult(entity.getCloseResult());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineWorkOrderPageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public MineWorkOrderPageVO setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
        return this;
    }

    public Long getAlarmEventId() {
        return alarmEventId;
    }

    public MineWorkOrderPageVO setAlarmEventId(Long alarmEventId) {
        this.alarmEventId = alarmEventId;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public MineWorkOrderPageVO setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineWorkOrderPageVO setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getWorkOrderTitle() {
        return workOrderTitle;
    }

    public MineWorkOrderPageVO setWorkOrderTitle(String workOrderTitle) {
        this.workOrderTitle = workOrderTitle;
        return this;
    }

    public String getWorkOrderContent() {
        return workOrderContent;
    }

    public MineWorkOrderPageVO setWorkOrderContent(String workOrderContent) {
        this.workOrderContent = workOrderContent;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineWorkOrderPageVO setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineWorkOrderPageVO setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineWorkOrderPageVO setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineWorkOrderPageVO setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineWorkOrderPageVO setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineWorkOrderPageVO setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineWorkOrderPageVO setLocation(String location) {
        this.location = location;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public MineWorkOrderPageVO setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Long getHandlerUserId() {
        return handlerUserId;
    }

    public MineWorkOrderPageVO setHandlerUserId(Long handlerUserId) {
        this.handlerUserId = handlerUserId;
        return this;
    }

    public String getHandlerUsername() {
        return handlerUsername;
    }

    public MineWorkOrderPageVO setHandlerUsername(String handlerUsername) {
        this.handlerUsername = handlerUsername;
        return this;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public MineWorkOrderPageVO setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
        return this;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public MineWorkOrderPageVO setHandleResult(String handleResult) {
        this.handleResult = handleResult;
        return this;
    }

    public Long getCloseUserId() {
        return closeUserId;
    }

    public MineWorkOrderPageVO setCloseUserId(Long closeUserId) {
        this.closeUserId = closeUserId;
        return this;
    }

    public String getCloseUsername() {
        return closeUsername;
    }

    public MineWorkOrderPageVO setCloseUsername(String closeUsername) {
        this.closeUsername = closeUsername;
        return this;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public MineWorkOrderPageVO setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    public String getCloseResult() {
        return closeResult;
    }

    public MineWorkOrderPageVO setCloseResult(String closeResult) {
        this.closeResult = closeResult;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineWorkOrderPageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineWorkOrderPageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineWorkOrderPageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
