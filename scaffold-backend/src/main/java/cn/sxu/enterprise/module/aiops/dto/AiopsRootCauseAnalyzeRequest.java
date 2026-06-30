package cn.sxu.enterprise.module.aiops.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AiopsRootCauseAnalyzeRequest {

    @NotNull(message = "告警事件ID不能为空")
    private Long alertEventId;

    @Min(value = 5, message = "回看分钟数至少为5")
    @Max(value = 1440, message = "回看分钟数不能超过1440")
    private Integer lookbackMinutes = 30;

    private String remark;

    public Long getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(Long alertEventId) {
        this.alertEventId = alertEventId;
    }

    public Integer getLookbackMinutes() {
        return lookbackMinutes;
    }

    public void setLookbackMinutes(Integer lookbackMinutes) {
        this.lookbackMinutes = lookbackMinutes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
