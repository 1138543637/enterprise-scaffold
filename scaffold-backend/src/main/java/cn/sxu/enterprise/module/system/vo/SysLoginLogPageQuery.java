package cn.sxu.enterprise.module.system.vo;

public class SysLoginLogPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String username;

    private Integer status;

    // 这里用 IDEA 生成 Getter / Setter

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}