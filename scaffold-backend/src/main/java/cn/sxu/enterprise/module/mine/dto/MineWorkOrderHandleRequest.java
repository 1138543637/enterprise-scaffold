package cn.sxu.enterprise.module.mine.dto;

import jakarta.validation.constraints.NotBlank;

public class MineWorkOrderHandleRequest {

    @NotBlank(message = "处理结果不能为空")
    private String handleResult;

    private String remark;

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
