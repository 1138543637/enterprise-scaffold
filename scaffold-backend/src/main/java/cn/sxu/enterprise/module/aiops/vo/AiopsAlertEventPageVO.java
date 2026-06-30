package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsAlertEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiopsAlertEventPageVO {

    private Long id;

    private String eventCode;

    private Long ruleId;

    private String ruleCode;

    private String ruleName;

    private Long metricDataId;

    private Long resourceId;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String metricCode;

    private String metricName;

    private String metricType;

    private BigDecimal metricValue;

    private BigDecimal thresholdValue;

    private String compareOperator;

    private Integer alertLevel;

    private String alertTitle;

    private String alertContent;

    private LocalDateTime alertTime;

    private Integer handleStatus;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static AiopsAlertEventPageVO fromEntity(AiopsAlertEvent entity) {
        if (entity == null) {
            return null;
        }
        AiopsAlertEventPageVO vo = new AiopsAlertEventPageVO();
        vo.setId(entity.getId());
        vo.setEventCode(entity.getEventCode());
        vo.setRuleId(entity.getRuleId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setMetricDataId(entity.getMetricDataId());
        vo.setResourceId(entity.getResourceId());
        vo.setResourceCode(entity.getResourceCode());
        vo.setResourceName(entity.getResourceName());
        vo.setResourceType(entity.getResourceType());
        vo.setIpAddr(entity.getIpAddr());
        vo.setMetricCode(entity.getMetricCode());
        vo.setMetricName(entity.getMetricName());
        vo.setMetricType(entity.getMetricType());
        vo.setMetricValue(entity.getMetricValue());
        vo.setThresholdValue(entity.getThresholdValue());
        vo.setCompareOperator(entity.getCompareOperator());
        vo.setAlertLevel(entity.getAlertLevel());
        vo.setAlertTitle(entity.getAlertTitle());
        vo.setAlertContent(entity.getAlertContent());
        vo.setAlertTime(entity.getAlertTime());
        vo.setHandleStatus(entity.getHandleStatus());
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

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getMetricDataId() {
        return metricDataId;
    }

    public void setMetricDataId(Long metricDataId) {
        this.metricDataId = metricDataId;
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

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    public Integer getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Integer alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public String getAlertContent() {
        return alertContent;
    }

    public void setAlertContent(String alertContent) {
        this.alertContent = alertContent;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalDateTime alertTime) {
        this.alertTime = alertTime;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
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
