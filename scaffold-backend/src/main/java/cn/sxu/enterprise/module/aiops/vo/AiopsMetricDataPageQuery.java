package cn.sxu.enterprise.module.aiops.vo;

public class AiopsMetricDataPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private Long resourceId;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String metricCode;

    private String metricType;

    private Integer alarmFlag;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public AiopsMetricDataPageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public AiopsMetricDataPageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public AiopsMetricDataPageQuery setResourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsMetricDataPageQuery setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsMetricDataPageQuery setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsMetricDataPageQuery setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsMetricDataPageQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public AiopsMetricDataPageQuery setMetricCode(String metricCode) {
        this.metricCode = metricCode;
        return this;
    }

    public String getMetricType() {
        return metricType;
    }

    public AiopsMetricDataPageQuery setMetricType(String metricType) {
        this.metricType = metricType;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public AiopsMetricDataPageQuery setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsMetricDataPageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
