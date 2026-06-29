package cn.sxu.enterprise.module.mine.vo;

public class MineWorkOrderStatusStatVO {

    private Integer orderStatus;
    private String orderStatusName;
    private Long total;

    public MineWorkOrderStatusStatVO() {
    }

    public MineWorkOrderStatusStatVO(Integer orderStatus, String orderStatusName, Long total) {
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatusName;
        this.total = total;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
