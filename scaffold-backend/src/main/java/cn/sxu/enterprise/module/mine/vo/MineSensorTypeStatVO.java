package cn.sxu.enterprise.module.mine.vo;

public class MineSensorTypeStatVO {

    private String sensorType;
    private String sensorTypeName;
    private Long total;

    public MineSensorTypeStatVO() {
    }

    public MineSensorTypeStatVO(String sensorType, String sensorTypeName, Long total) {
        this.sensorType = sensorType;
        this.sensorTypeName = sensorTypeName;
        this.total = total;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorTypeName() {
        return sensorTypeName;
    }

    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
