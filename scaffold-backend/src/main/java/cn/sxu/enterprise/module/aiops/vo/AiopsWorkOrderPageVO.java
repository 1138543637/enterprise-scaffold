package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsWorkOrder;
import java.time.LocalDateTime;

public class AiopsWorkOrderPageVO {

    private Long id;

    private String workOrderCode;

    private Long alertEventId;

    private String eventCode;

    private Integer alertLevel;

    private String workOrderTitle;

    private String workOrderContent;

    private Long resourceId;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String metricCode;

    private String metricName;

    private String metricType;

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

    public static AiopsWorkOrderPageVO fromEntity(AiopsWorkOrder entity) {
        if (entity == null) {
            return null;
        }
        AiopsWorkOrderPageVO vo = new AiopsWorkOrderPageVO();
        vo.setId(entity.getId());
        vo.setWorkOrderCode(entity.getWorkOrderCode());
        vo.setAlertEventId(entity.getAlertEventId());
        vo.setEventCode(entity.getEventCode());
        vo.setAlertLevel(entity.getAlertLevel());
        vo.setWorkOrderTitle(entity.getWorkOrderTitle());
        vo.setWorkOrderContent(entity.getWorkOrderContent());
        vo.setResourceId(entity.getResourceId());
        vo.setResourceCode(entity.getResourceCode());
        vo.setResourceName(entity.getResourceName());
        vo.setResourceType(entity.getResourceType());
        vo.setIpAddr(entity.getIpAddr());
        vo.setMetricCode(entity.getMetricCode());
        vo.setMetricName(entity.getMetricName());
        vo.setMetricType(entity.getMetricType());
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public Long getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(Long alertEventId) {
        this.alertEventId = alertEventId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public Integer getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Integer alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getWorkOrderTitle() {
        return workOrderTitle;
    }

    public void setWorkOrderTitle(String workOrderTitle) {
        this.workOrderTitle = workOrderTitle;
    }

    public String getWorkOrderContent() {
        return workOrderContent;
    }

    public void setWorkOrderContent(String workOrderContent) {
        this.workOrderContent = workOrderContent;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getHandlerUserId() {
        return handlerUserId;
    }

    public void setHandlerUserId(Long handlerUserId) {
        this.handlerUserId = handlerUserId;
    }

    public String getHandlerUsername() {
        return handlerUsername;
    }

    public void setHandlerUsername(String handlerUsername) {
        this.handlerUsername = handlerUsername;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Long getCloseUserId() {
        return closeUserId;
    }

    public void setCloseUserId(Long closeUserId) {
        this.closeUserId = closeUserId;
    }

    public String getCloseUsername() {
        return closeUsername;
    }

    public void setCloseUsername(String closeUsername) {
        this.closeUsername = closeUsername;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseResult() {
        return closeResult;
    }

    public void setCloseResult(String closeResult) {
        this.closeResult = closeResult;
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
