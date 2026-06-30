package cn.sxu.enterprise.module.aiops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("aiops_resource")
public class AiopsResource {

    @TableId(type = IdType.AUTO)
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

    public AiopsResource setId(Long id) {
        this.id = id;
        return this;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public AiopsResource setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
        return this;
    }

    public String getResourceName() {
        return resourceName;
    }

    public AiopsResource setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public String getResourceType() {
        return resourceType;
    }

    public AiopsResource setResourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AiopsResource setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public AiopsResource setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getEnvType() {
        return envType;
    }

    public AiopsResource setEnvType(String envType) {
        this.envType = envType;
        return this;
    }

    public String getSystemName() {
        return systemName;
    }

    public AiopsResource setSystemName(String systemName) {
        this.systemName = systemName;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public AiopsResource setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public Integer getCollectEnabled() {
        return collectEnabled;
    }

    public AiopsResource setCollectEnabled(Integer collectEnabled) {
        this.collectEnabled = collectEnabled;
        return this;
    }

    public LocalDateTime getLastCollectTime() {
        return lastCollectTime;
    }

    public AiopsResource setLastCollectTime(LocalDateTime lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiopsResource setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public AiopsResource setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AiopsResource setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public AiopsResource setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AiopsResource setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public AiopsResource setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AiopsResource setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
