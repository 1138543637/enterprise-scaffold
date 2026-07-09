package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IamSecurityPolicyPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String policyCode;

    private String policyName;

    private String policyType;

    private Integer policyLevel;

    private String effectiveScope;

    private Integer enabled;

    private Integer status;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
