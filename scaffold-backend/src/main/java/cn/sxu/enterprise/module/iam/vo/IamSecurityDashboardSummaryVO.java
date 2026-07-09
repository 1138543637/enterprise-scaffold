package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamSecurityDashboardSummaryVO {

    private Long todayAccessCount;

    private Long todayFailedAccessCount;

    private Long unhandledLoginRiskCount;

    private Long highRiskPermissionAuditCount;

    private Long pendingAuditCount;

    private Long enabledSecurityPolicyCount;

    private Long enabledRateLimitRuleCount;
}
