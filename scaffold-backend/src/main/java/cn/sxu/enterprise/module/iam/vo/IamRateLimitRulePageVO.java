package cn.sxu.enterprise.module.iam.vo;

import cn.sxu.enterprise.module.iam.entity.IamRateLimitRule;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IamRateLimitRulePageVO {

    private Long id;

    private String ruleCode;

    private String ruleName;

    private String requestUri;

    private String requestMethod;

    private String limitDimension;

    private String limitDimensionName;

    private Integer limitWindowSeconds;

    private Integer maxRequests;

    private String limitAction;

    private String limitActionName;

    private Integer enabled;

    private String enabledName;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static IamRateLimitRulePageVO fromEntity(IamRateLimitRule entity) {
        IamRateLimitRulePageVO vo = new IamRateLimitRulePageVO();
        vo.setId(entity.getId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setRequestUri(entity.getRequestUri());
        vo.setRequestMethod(entity.getRequestMethod());
        vo.setLimitDimension(entity.getLimitDimension());
        vo.setLimitDimensionName(limitDimensionName(entity.getLimitDimension()));
        vo.setLimitWindowSeconds(entity.getLimitWindowSeconds());
        vo.setMaxRequests(entity.getMaxRequests());
        vo.setLimitAction(entity.getLimitAction());
        vo.setLimitActionName(limitActionName(entity.getLimitAction()));
        vo.setEnabled(entity.getEnabled());
        vo.setEnabledName(enabledName(entity.getEnabled()));
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String limitDimensionName(String value) {
        if ("GLOBAL".equals(value)) {
            return "全局";
        }
        if ("USER".equals(value)) {
            return "用户";
        }
        if ("IP".equals(value)) {
            return "客户端 IP";
        }
        return "未知";
    }

    private static String limitActionName(String value) {
        if ("WARN".equals(value)) {
            return "仅告警";
        }
        if ("REJECT".equals(value)) {
            return "拒绝请求";
        }
        return "未知";
    }

    private static String enabledName(Integer value) {
        if (Integer.valueOf(1).equals(value)) {
            return "启用";
        }
        if (Integer.valueOf(0).equals(value)) {
            return "停用";
        }
        return "未知";
    }
}
