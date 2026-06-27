package cn.sxu.enterprise.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("sys_file")
public class SysFile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String fileName;
    private String originalName;
    private String fileType;
    private Long fileSize;
    private String storageType;
    private String bucketName;
    private String objectName;
    private String url;
    private String md5;
    private Long uploadUserId;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer deleted;
    private String remark;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getFileType() {
        return fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getStorageType() {
        return storageType;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}