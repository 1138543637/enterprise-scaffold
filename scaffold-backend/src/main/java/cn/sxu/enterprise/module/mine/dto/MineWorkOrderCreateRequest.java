package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.NotNull;

public class MineWorkOrderCreateRequest {

    @NotNull(message = "告警事件ID不能为空")
    private Long alarmEventId;

    private String remark;

    public Long getAlarmEventId() {
        return alarmEventId;
    }

    public void setAlarmEventId(Long alarmEventId) {
        this.alarmEventId = alarmEventId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
