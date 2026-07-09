package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

/**
 * IAM 异常登录风险处理请求。
 */
@Data
public class IamLoginRiskHandleRequest {

    /**
     * 处理人。
     */
    private String handleBy;

    /**
     * 处理备注。
     */
    private String handleRemark;
}
