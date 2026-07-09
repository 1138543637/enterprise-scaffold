package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

/**
 * IAM 风险闭环统计 VO。
 */
@Data
public class IamRiskClosureSummaryVO {

    private Long unhandledLoginRiskCount;

    private Long confirmedLoginRiskCount;

    private Long ignoredLoginRiskCount;

    private Long closedLoginRiskCount;

    private Long pendingAuditCount;

    private Long reviewedAuditCount;

    private Long ignoredAuditCount;
}
