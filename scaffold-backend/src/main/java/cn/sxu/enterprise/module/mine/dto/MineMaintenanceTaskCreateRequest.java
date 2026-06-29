package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.NotNull;

public class MineMaintenanceTaskCreateRequest {

    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    private String remark;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
