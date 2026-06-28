package cn.sxu.enterprise.module.mine.vo;

public class MineSensorDataPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private Long sensorId;

    private String sensorCode;

    private String sensorName;

    private String sensorType;

    private String areaName;

    private Integer alarmFlag;

    private Integer status;

    public Long getPageNo() {
        return pageNo;
    }

    public MineSensorDataPageQuery setPageNo(Long pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public MineSensorDataPageQuery setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public MineSensorDataPageQuery setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public MineSensorDataPageQuery setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
        return this;
    }

    public String getSensorName() {
        return sensorName;
    }

    public MineSensorDataPageQuery setSensorName(String sensorName) {
        this.sensorName = sensorName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorDataPageQuery setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public MineSensorDataPageQuery setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public Integer getAlarmFlag() {
        return alarmFlag;
    }

    public MineSensorDataPageQuery setAlarmFlag(Integer alarmFlag) {
        this.alarmFlag = alarmFlag;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineSensorDataPageQuery setStatus(Integer status) {
        this.status = status;
        return this;
    }
}