package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamPolicyStatusStatVO {

    private Integer enabled;

    private String enabledName;

    private Long securityPolicyCount;

    private Long rateLimitRuleCount;

    private Long totalCount;
}
