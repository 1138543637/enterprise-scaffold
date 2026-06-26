package cn.sxu.enterprise.common.core.page;

import java.util.List;

public class PageResult<T> {

    private Long pageNo;

    private Long pageSize;

    private Long total;

    private Long pages;

    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long pageNo, Long pageSize, Long total, Long pages, List<T> records) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
        this.records = records;
    }

    public static <T> PageResult<T> of(Long pageNo, Long pageSize, Long total, Long pages, List<T> records) {
        return new PageResult<>(pageNo, pageSize, total, pages, records);
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}