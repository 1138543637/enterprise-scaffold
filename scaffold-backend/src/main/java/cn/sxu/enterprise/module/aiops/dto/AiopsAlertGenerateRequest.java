package cn.sxu.enterprise.module.aiops.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public class AiopsAlertGenerateRequest {

    private Long metricDataId;

    private String resourceType;

    private String metricType;

    @Min(value = 1, message = "最多扫描数据条数至少为1")
    @Max(value = 1000, message = "最多扫描数据条数不能超过1000")
    private Integer limit = 100;

    public Long getMetricDataId() {
        return metricDataId;
    }

    public void setMetricDataId(Long metricDataId) {
        this.metricDataId = metricDataId;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
