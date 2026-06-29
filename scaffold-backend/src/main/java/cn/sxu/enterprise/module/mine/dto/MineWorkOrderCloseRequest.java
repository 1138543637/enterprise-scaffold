package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.NotBlank;

public class MineWorkOrderCloseRequest {

    @NotBlank(message = "关闭结果不能为空")
    private String closeResult;

    private String remark;

    public String getCloseResult() {
        return closeResult;
    }

    public void setCloseResult(String closeResult) {
        this.closeResult = closeResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
