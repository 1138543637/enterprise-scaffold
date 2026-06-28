package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class MineSensorDataSimulateRequest {

    private Long sensorId;

    private String sensorType;

    @Min(value = 1, message = "每个传感器至少生成1条数据")
    @Max(value = 20, message = "每个传感器最多生成20条数据")
    private Integer count = 1;

    public Long getSensorId() {
        return sensorId;
    }

    public MineSensorDataSimulateRequest setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineSensorDataSimulateRequest setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public MineSensorDataSimulateRequest setCount(Integer count) {
        this.count = count;
        return this;
    }
}