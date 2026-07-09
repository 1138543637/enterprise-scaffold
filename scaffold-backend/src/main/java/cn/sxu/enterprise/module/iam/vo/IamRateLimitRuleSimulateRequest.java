package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

@Data
public class IamRateLimitRuleSimulateRequest {

    private String requestUri;

    private String requestMethod;

    private String username;

    private String clientIp;

    private Integer currentRequests;
}
