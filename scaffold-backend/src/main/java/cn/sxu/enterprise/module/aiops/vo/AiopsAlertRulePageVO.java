package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsAlertRule;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiopsAlertRulePageVO {

    private Long id;

    private String ruleCode;

    private String ruleName;

    private String resourceType;

    private String metricType;

    private String compareOperator;

    private BigDecimal thresholdValue;

    private Integer alertLevel;

    private String alertTitle;

    private String alertContent;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static AiopsAlertRulePageVO fromEntity(AiopsAlertRule entity) {
        if (entity == null) {
            return null;
        }
        AiopsAlertRulePageVO vo = new AiopsAlertRulePageVO();
        vo.setId(entity.getId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setResourceType(entity.getResourceType());
        vo.setMetricType(entity.getMetricType());
        vo.setCompareOperator(entity.getCompareOperator());
        vo.setThresholdValue(entity.getThresholdValue());
        vo.setAlertLevel(entity.getAlertLevel());
        vo.setAlertTitle(entity.getAlertTitle());
        vo.setAlertContent(entity.getAlertContent());
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
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
