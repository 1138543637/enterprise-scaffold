package cn.sxu.enterprise.module.aiops.vo;

import cn.sxu.enterprise.module.aiops.entity.AiopsResource;

import java.time.LocalDateTime;

public class AiopsResourcePageVO {

    private Long id;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String ipAddr;

    private Integer port;

    private String envType;

    private String systemName;

    private String ownerName;

    private Integer collectEnabled;

    private LocalDateTime lastCollectTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static AiopsResourcePageVO fromEntity(AiopsResource entity) {
        if (entity == null) {
            return null;
        }

        AiopsResourcePageVO vo = new AiopsResourcePageVO();
        vo.setId(entity.getId());
        vo.setResourceCode(entity.getResourceCode());
        vo.setResourceName(entity.getResourceName());
        vo.setResourceType(entity.getResourceType());
        vo.setIpAddr(entity.getIpAddr());
        vo.setPort(entity.getPort());
        vo.setEnvType(entity.getEnvType());
        vo.setSystemName(entity.getSystemName());
        vo.setOwnerName(entity.getOwnerName());
        vo.setCollectEnabled(entity.getCollectEnabled());
        vo.setLastCollectTime(entity.getLastCollectTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public AiopsResourcePageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsResourcePageVO setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsResourcePageVO setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsResourcePageVO setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsResourcePageVO setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public AiopsResourcePageVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getEnvType() {
        return envType;
    }

    public AiopsResourcePageVO setEnvType(String envType) {
        this.envType = envType;
        return this;
    }

    public String getSystemName() {
        return systemName;
    }

    public AiopsResourcePageVO setSystemName(String systemName) {
        this.systemName = systemName;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public AiopsResourcePageVO setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public Integer getCollectEnabled() {
        return collectEnabled;
    }

    public AiopsResourcePageVO setCollectEnabled(Integer collectEnabled) {
        this.collectEnabled = collectEnabled;
        return this;
    }

    public LocalDateTime getLastCollectTime() {
        return lastCollectTime;
    }

    public AiopsResourcePageVO setLastCollectTime(LocalDateTime lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsResourcePageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AiopsResourcePageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AiopsResourcePageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
