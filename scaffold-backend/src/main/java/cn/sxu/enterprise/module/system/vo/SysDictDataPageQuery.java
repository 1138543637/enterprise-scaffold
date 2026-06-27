package cn.sxu.enterprise.module.system.vo;

public class SysDictDataPageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;
    private String dictType;
    private String dictLabel;
    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public String getDictType() {
        return dictType;
    }

    public String getDictLabel() {
        return dictLabel;
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

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
