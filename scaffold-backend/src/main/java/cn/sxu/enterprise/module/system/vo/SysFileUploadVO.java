package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysFile;

public class SysFileUploadVO {

    private Long id;
    private String fileName;
    private String originalName;
    private String fileType;
    private Long fileSize;
    private String storageType;
    private String objectName;
    private String url;
    private String md5;

    public static SysFileUploadVO fromEntity(SysFile entity) {
        SysFileUploadVO vo = new SysFileUploadVO();
        vo.setId(entity.getId());
        vo.setFileName(entity.getFileName());
        vo.setOriginalName(entity.getOriginalName());
        vo.setFileType(entity.getFileType());
        vo.setFileSize(entity.getFileSize());
        vo.setStorageType(entity.getStorageType());
        vo.setObjectName(entity.getObjectName());
        vo.setUrl(entity.getUrl());
        vo.setMd5(entity.getMd5());
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

    public String getObjectName() {
        return objectName;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
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

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
