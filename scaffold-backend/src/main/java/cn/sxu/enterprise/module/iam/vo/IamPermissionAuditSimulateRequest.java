package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamPermissionAuditSimulateRequest {

    private String auditType;

    private String targetType;

    private Long targetId;

    private String targetName;

    private String changeAction;

    private String beforeValue;

    private String afterValue;

    private String riskLevel;

    private String requestIp;

    private String operatorName;

    private String remark;
}
