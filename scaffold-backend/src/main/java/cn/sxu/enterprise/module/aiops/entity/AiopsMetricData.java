package cn.sxu.enterprise.module.aiops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("aiops_metric_data")
public class AiopsMetricData {

    @TableId(type = IdType.AUTO)
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

    public AiopsMetricData setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public AiopsMetricData setResourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsMetricData setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsMetricData setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsMetricData setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsMetricData setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public AiopsMetricData setMetricCode(String metricCode) {
        this.metricCode = metricCode;
        return this;
    }

    public String getMetricName() {
        return metricName;
    }

    public AiopsMetricData setMetricName(String metricName) {
        this.metricName = metricName;
        return this;
    }

    public String getMetricType() {
        return metricType;
    }

    public AiopsMetricData setMetricType(String metricType) {
        this.metricType = metricType;
        return this;
    }

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public AiopsMetricData setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
        return this;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public AiopsMetricData setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public AiopsMetricData setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public AiopsMetricData setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public AiopsMetricData setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsMetricData setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public AiopsMetricData setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AiopsMetricData setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public AiopsMetricData setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AiopsMetricData setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public AiopsMetricData setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AiopsMetricData setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
