package cn.sxu.enterprise.module.aiops.dto;

import jakarta.validation.constraints.NotNull;


public class AiopsWorkOrderCreateRequest {

    @NotNull(message = "告警事件ID不能为空")
    private Long alertEventId;

    private String remark;

    public Long getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(Long alertEventId) {
        this.alertEventId = alertEventId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
