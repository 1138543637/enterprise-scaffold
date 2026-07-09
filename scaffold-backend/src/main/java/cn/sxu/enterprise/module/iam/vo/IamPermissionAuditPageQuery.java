package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IamPermissionAuditPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String auditCode;

    private String auditType;

    private String targetType;

    private String targetName;

    private String changeAction;

    private String riskLevel;

    private String reviewStatus;

    private String operatorName;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
