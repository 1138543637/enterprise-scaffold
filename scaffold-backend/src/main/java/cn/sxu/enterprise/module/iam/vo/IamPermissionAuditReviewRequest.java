package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamPermissionAuditReviewRequest {

    private String reviewStatus;

    private String reviewBy;

    private String remark;
}
