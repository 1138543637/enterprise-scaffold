package cn.sxu.enterprise.module.mine.vo;

public class MineDevicePageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String deviceCode;

    private String deviceName;

    private String deviceType;

    private String areaName;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public MineDevicePageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public MineDevicePageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public MineDevicePageQuery setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public MineDevicePageQuery setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public MineDevicePageQuery setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineDevicePageQuery setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineDevicePageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}