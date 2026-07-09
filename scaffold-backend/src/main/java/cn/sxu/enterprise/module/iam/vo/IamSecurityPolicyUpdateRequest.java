package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamSecurityPolicyUpdateRequest {

    private String policyValue;

    private String policyUnit;

    private String effectiveScope;

    private Integer enabled;

    private String remark;
}
