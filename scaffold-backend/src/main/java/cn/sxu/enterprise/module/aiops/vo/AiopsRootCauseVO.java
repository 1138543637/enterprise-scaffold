package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsRootCauseRecord;

import java.time.LocalDateTime;

public class AiopsRootCauseVO {

    private Long id;

    private String analysisCode;

    private Long alertEventId;

    private String eventCode;

    private Long resourceId;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String rootCauseType;

    private String rootCauseDesc;

    private String evidence;

    private String suggestion;

    private Integer confidenceScore;

    private LocalDateTime analysisTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static AiopsRootCauseVO fromEntity(AiopsRootCauseRecord entity) {
        if (entity == null) {
            return null;
        }
        AiopsRootCauseVO vo = new AiopsRootCauseVO();
        vo.setId(entity.getId());
        vo.setAnalysisCode(entity.getAnalysisCode());
        vo.setAlertEventId(entity.getAlertEventId());
        vo.setEventCode(entity.getEventCode());
        vo.setResourceId(entity.getResourceId());
        vo.setResourceCode(entity.getResourceCode());
        vo.setResourceName(entity.getResourceName());
        vo.setResourceType(entity.getResourceType());
        vo.setIpAddr(entity.getIpAddr());
        vo.setRootCauseType(entity.getRootCauseType());
        vo.setRootCauseDesc(entity.getRootCauseDesc());
        vo.setEvidence(entity.getEvidence());
        vo.setSuggestion(entity.getSuggestion());
        vo.setConfidenceScore(entity.getConfidenceScore());
        vo.setAnalysisTime(entity.getAnalysisTime());
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

    public String getAnalysisCode() {
        return analysisCode;
    }

    public void setAnalysisCode(String analysisCode) {
        this.analysisCode = analysisCode;
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

    public String getRootCauseType() {
        return rootCauseType;
    }

    public void setRootCauseType(String rootCauseType) {
        this.rootCauseType = rootCauseType;
    }

    public String getRootCauseDesc() {
        return rootCauseDesc;
    }

    public void setRootCauseDesc(String rootCauseDesc) {
        this.rootCauseDesc = rootCauseDesc;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public LocalDateTime getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(LocalDateTime analysisTime) {
        this.analysisTime = analysisTime;
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
