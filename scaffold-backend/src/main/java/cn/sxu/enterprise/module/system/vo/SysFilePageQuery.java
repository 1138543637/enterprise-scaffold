package cn.sxu.enterprise.module.system.vo;

public class SysFilePageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;
    private String originalName;
    private String fileType;
    private String storageType;
    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getStorageType() {
        return storageType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
