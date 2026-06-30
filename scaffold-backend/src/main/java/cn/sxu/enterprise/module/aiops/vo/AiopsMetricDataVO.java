package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsMetricData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiopsMetricDataVO {

    private Long id;

    private Long resourceId;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String metricCode;

    private String metricName;

    private String metricType;

    private BigDecimal metricValue;

    private String metricUnit;

    private BigDecimal thresholdValue;

    private Integer alarmFlag;

    private LocalDateTime collectTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static AiopsMetricDataVO fromEntity(AiopsMetricData entity) {
        if (entity == null) {
            return null;
        }

        AiopsMetricDataVO vo = new AiopsMetricDataVO();
        vo.setId(entity.getId());
        vo.setResourceId(entity.getResourceId());
        vo.setResourceCode(entity.getResourceCode());
        vo.setResourceName(entity.getResourceName());
        vo.setResourceType(entity.getResourceType());
        vo.setIpAddr(entity.getIpAddr());
        vo.setMetricCode(entity.getMetricCode());
        vo.setMetricName(entity.getMetricName());
        vo.setMetricType(entity.getMetricType());
        vo.setMetricValue(entity.getMetricValue());
        vo.setMetricUnit(entity.getMetricUnit());
        vo.setThresholdValue(entity.getThresholdValue());
        vo.setAlarmFlag(entity.getAlarmFlag());
        vo.setCollectTime(entity.getCollectTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public AiopsMetricDataVO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public AiopsMetricDataVO setResourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsMetricDataVO setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsMetricDataVO setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsMetricDataVO setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsMetricDataVO setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public AiopsMetricDataVO setMetricCode(String metricCode) {
        this.metricCode = metricCode;
        return this;
    }

    public String getMetricName() {
        return metricName;
    }

    public AiopsMetricDataVO setMetricName(String metricName) {
        this.metricName = metricName;
        return this;
    }

    public String getMetricType() {
        return metricType;
    }

    public AiopsMetricDataVO setMetricType(String metricType) {
        this.metricType = metricType;
        return this;
    }

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public AiopsMetricDataVO setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
        return this;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public AiopsMetricDataVO setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public AiopsMetricDataVO setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public AiopsMetricDataVO setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public AiopsMetricDataVO setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsMetricDataVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AiopsMetricDataVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AiopsMetricDataVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
