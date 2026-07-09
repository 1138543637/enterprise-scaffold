package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamRateLimitRuleSimulateVO {

    private Boolean matched;

    private Boolean allowed;

    private Integer hitRuleCount;

    private Long ruleId;

    private String ruleCode;

    private String ruleName;

    private String requestUri;

    private String requestMethod;

    private String limitDimension;

    private String limitDimensionName;

    private Integer limitWindowSeconds;

    private Integer maxRequests;

    private Integer currentRequests;

    private Integer remainRequests;

    private String message;
}
