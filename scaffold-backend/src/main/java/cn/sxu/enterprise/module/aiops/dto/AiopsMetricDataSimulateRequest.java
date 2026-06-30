package cn.sxu.enterprise.module.aiops.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class AiopsMetricDataSimulateRequest {

    private Long resourceId;

    private String resourceType;

    private String metricType;

    @Min(value = 1, message = "每个资源至少生成1轮指标数据")
    @Max(value = 20, message = "每个资源最多生成20轮指标数据")
    private Integer count = 1;

    private String remark;

    public Long getResourceId() {
        return resourceId;
    }

    public AiopsMetricDataSimulateRequest setResourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsMetricDataSimulateRequest setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getMetricType() {
        return metricType;
    }

    public AiopsMetricDataSimulateRequest setMetricType(String metricType) {
        this.metricType = metricType;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public AiopsMetricDataSimulateRequest setCount(Integer count) {
        this.count = count;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AiopsMetricDataSimulateRequest setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
