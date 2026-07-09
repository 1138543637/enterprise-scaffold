package cn.sxu.enterprise.module.iam.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IamRateLimitRulePageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String ruleCode;

    private String ruleName;

    private String requestUri;

    private String requestMethod;

    private String limitDimension;

    private Integer enabled;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
