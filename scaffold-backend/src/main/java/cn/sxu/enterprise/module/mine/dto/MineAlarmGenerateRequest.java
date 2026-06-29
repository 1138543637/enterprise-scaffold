package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class MineAlarmGenerateRequest {

    private Long sensorDataId;

    private String sensorType;

    @Min(value = 1, message = "最多扫描数据条数至少为1")
    @Max(value = 1000, message = "最多扫描数据条数不能超过1000")
    private Integer limit = 100;

    public Long getSensorDataId() {
        return sensorDataId;
    }

    public MineAlarmGenerateRequest setSensorDataId(Long sensorDataId) {
        this.sensorDataId = sensorDataId;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmGenerateRequest setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public MineAlarmGenerateRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
