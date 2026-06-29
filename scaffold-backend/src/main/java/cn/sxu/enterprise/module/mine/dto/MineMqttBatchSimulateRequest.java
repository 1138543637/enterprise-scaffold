package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class MineMqttBatchSimulateRequest {

    private String sensorType;

    @Min(value = 1, message = "模拟条数不能小于1")
    @Max(value = 100, message = "模拟条数不能大于100")
    private Integer count = 10;

    @Min(value = 0, message = "发送间隔不能小于0毫秒")
    @Max(value = 1000, message = "发送间隔不能大于1000毫秒")
    private Integer intervalMillis = 0;

    private String remark;

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIntervalMillis() {
        return intervalMillis;
    }

    public void setIntervalMillis(Integer intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
