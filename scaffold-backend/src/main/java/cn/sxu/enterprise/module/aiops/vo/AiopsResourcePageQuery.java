package cn.sxu.enterprise.module.aiops.vo;

public class AiopsResourcePageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private String envType;

    private String systemName;

    private String ownerName;

    private Integer collectEnabled;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public AiopsResourcePageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public AiopsResourcePageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsResourcePageQuery setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsResourcePageQuery setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsResourcePageQuery setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsResourcePageQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getEnvType() {
        return envType;
    }

    public AiopsResourcePageQuery setEnvType(String envType) {
        this.envType = envType;
        return this;
    }

    public String getSystemName() {
        return systemName;
    }

    public AiopsResourcePageQuery setSystemName(String systemName) {
        this.systemName = systemName;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public AiopsResourcePageQuery setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public Integer getCollectEnabled() {
        return collectEnabled;
    }

    public AiopsResourcePageQuery setCollectEnabled(Integer collectEnabled) {
        this.collectEnabled = collectEnabled;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsResourcePageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
