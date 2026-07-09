package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

/**
 * IAM 权限审计闭环处理请求。
 */
@Data
public class IamPermissionAuditClosureRequest {

    /**
     * 复核人。
     */
    private String reviewBy;

    /**
     * 备注。
     */
    private String remark;
}
