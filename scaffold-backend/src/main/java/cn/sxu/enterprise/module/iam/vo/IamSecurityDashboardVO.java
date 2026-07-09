package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IamSecurityDashboardVO {

    private IamSecurityDashboardSummaryVO summary;

    private List<IamRiskDistributionVO> riskDistributions = new ArrayList<>();

    private List<IamReviewStatusStatVO> reviewStatusStats = new ArrayList<>();

    private List<IamPolicyStatusStatVO> policyStatusStats = new ArrayList<>();

    private List<IamRecentSecurityEventVO> recentEvents = new ArrayList<>();
}
