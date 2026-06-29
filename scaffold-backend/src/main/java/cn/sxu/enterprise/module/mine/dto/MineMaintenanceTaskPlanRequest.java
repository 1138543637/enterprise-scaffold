package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MineMaintenanceTaskPlanRequest {

    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime planStartTime;

    @NotNull(message = "计划结束时间不能为空")
    private LocalDateTime planEndTime;

    @NotBlank(message = "维护人员不能为空")
    private String maintainerUsername;

    private Long maintainerUserId;

    private String remark;

    public LocalDateTime getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(LocalDateTime planStartTime) {
        this.planStartTime = planStartTime;
    }

    public LocalDateTime getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(LocalDateTime planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getMaintainerUsername() {
        return maintainerUsername;
    }

    public void setMaintainerUsername(String maintainerUsername) {
        this.maintainerUsername = maintainerUsername;
    }

    public Long getMaintainerUserId() {
        return maintainerUserId;
    }

    public void setMaintainerUserId(Long maintainerUserId) {
        this.maintainerUserId = maintainerUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
