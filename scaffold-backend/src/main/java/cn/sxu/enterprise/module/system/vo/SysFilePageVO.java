package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysFile;

import java.time.LocalDateTime;

public class SysFilePageVO {

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
    private LocalDateTime createTime;
    private String remark;

    public static SysFilePageVO fromEntity(SysFile entity) {
        SysFilePageVO vo = new SysFilePageVO();
        vo.setId(entity.getId());
        vo.setFileName(entity.getFileName());
        vo.setOriginalName(entity.getOriginalName());
        vo.setFileType(entity.getFileType());
        vo.setFileSize(entity.getFileSize());
        vo.setStorageType(entity.getStorageType());
        vo.setBucketName(entity.getBucketName());
        vo.setObjectName(entity.getObjectName());
        vo.setUrl(entity.getUrl());
        vo.setMd5(entity.getMd5());
        vo.setUploadUserId(entity.getUploadUserId());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
