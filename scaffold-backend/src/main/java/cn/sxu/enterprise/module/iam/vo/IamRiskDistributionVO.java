package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamRiskDistributionVO {

    private String riskLevel;

    private String riskLevelName;

    private Long loginRiskCount;

    private Long permissionAuditCount;

    private Long totalCount;
}
