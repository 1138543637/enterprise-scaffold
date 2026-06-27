package cn.sxu.enterprise.module.system.vo;

public class SysDictTypePageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;
    private String dictName;
    private String dictType;
    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public String getDictName() {
        return dictName;
    }

    public String getDictType() {
        return dictType;
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

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
