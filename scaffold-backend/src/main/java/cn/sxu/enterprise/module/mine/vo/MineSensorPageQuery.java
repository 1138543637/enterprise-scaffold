package cn.sxu.enterprise.module.mine.vo;

public class MineSensorPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private Long deviceId;

    private String areaName;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public MineSensorPageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public MineSensorPageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensorPageQuery setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensorPageQuery setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorPageQuery setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public MineSensorPageQuery setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensorPageQuery setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensorPageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}