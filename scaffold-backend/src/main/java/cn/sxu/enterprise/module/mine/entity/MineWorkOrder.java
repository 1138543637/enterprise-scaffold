package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("mine_work_order")
public class MineWorkOrder {

    @TableId(type = IdType.AUTO)
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

    public MineWorkOrder setId(Long id) {
        this.id = id;
        return this;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public MineWorkOrder setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
        return this;
    }

    public Long getAlarmEventId() {
        return alarmEventId;
    }

    public MineWorkOrder setAlarmEventId(Long alarmEventId) {
        this.alarmEventId = alarmEventId;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public MineWorkOrder setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineWorkOrder setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getWorkOrderTitle() {
        return workOrderTitle;
    }

    public MineWorkOrder setWorkOrderTitle(String workOrderTitle) {
        this.workOrderTitle = workOrderTitle;
        return this;
    }

    public String getWorkOrderContent() {
        return workOrderContent;
    }

    public MineWorkOrder setWorkOrderContent(String workOrderContent) {
        this.workOrderContent = workOrderContent;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineWorkOrder setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineWorkOrder setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineWorkOrder setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineWorkOrder setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineWorkOrder setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineWorkOrder setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public MineWorkOrder setLocation(String location) {
        this.location = location;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public MineWorkOrder setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Long getHandlerUserId() {
        return handlerUserId;
    }

    public MineWorkOrder setHandlerUserId(Long handlerUserId) {
        this.handlerUserId = handlerUserId;
        return this;
    }

    public String getHandlerUsername() {
        return handlerUsername;
    }

    public MineWorkOrder setHandlerUsername(String handlerUsername) {
        this.handlerUsername = handlerUsername;
        return this;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public MineWorkOrder setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
        return this;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public MineWorkOrder setHandleResult(String handleResult) {
        this.handleResult = handleResult;
        return this;
    }

    public Long getCloseUserId() {
        return closeUserId;
    }

    public MineWorkOrder setCloseUserId(Long closeUserId) {
        this.closeUserId = closeUserId;
        return this;
    }

    public String getCloseUsername() {
        return closeUsername;
    }

    public MineWorkOrder setCloseUsername(String closeUsername) {
        this.closeUsername = closeUsername;
        return this;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public MineWorkOrder setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    public String getCloseResult() {
        return closeResult;
    }

    public MineWorkOrder setCloseResult(String closeResult) {
        this.closeResult = closeResult;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineWorkOrder setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineWorkOrder setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineWorkOrder setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineWorkOrder setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineWorkOrder setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineWorkOrder setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineWorkOrder setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
