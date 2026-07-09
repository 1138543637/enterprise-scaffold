package cn.sxu.enterprise.module.iam.vo;

import cn.sxu.enterprise.module.iam.entity.IamSecurityPolicy;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IamSecurityPolicyPageVO {

    private Long id;

    private String policyCode;

    private String policyName;

    private String policyType;

    private String policyTypeName;

    private Integer policyLevel;

    private String policyLevelName;

    private String policyValue;

    private String policyUnit;

    private String policyUnitName;

    private String effectiveScope;

    private String effectiveScopeName;

    private Integer enabled;

    private String enabledName;

    private Integer status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String remark;

    public static IamSecurityPolicyPageVO fromEntity(IamSecurityPolicy entity) {
        if (entity == null) {
            return null;
        }
        IamSecurityPolicyPageVO vo = new IamSecurityPolicyPageVO();
        vo.setId(entity.getId());
        vo.setPolicyCode(entity.getPolicyCode());
        vo.setPolicyName(entity.getPolicyName());
        vo.setPolicyType(entity.getPolicyType());
        vo.setPolicyTypeName(policyTypeName(entity.getPolicyType()));
        vo.setPolicyLevel(entity.getPolicyLevel());
        vo.setPolicyLevelName(policyLevelName(entity.getPolicyLevel()));
        vo.setPolicyValue(entity.getPolicyValue());
        vo.setPolicyUnit(entity.getPolicyUnit());
        vo.setPolicyUnitName(policyUnitName(entity.getPolicyUnit()));
        vo.setEffectiveScope(entity.getEffectiveScope());
        vo.setEffectiveScopeName(effectiveScopeName(entity.getEffectiveScope()));
        vo.setEnabled(entity.getEnabled());
        vo.setEnabledName(enabledName(entity.getEnabled()));
        vo.setStatus(entity.getStatus());
        vo.setStatusName(statusName(entity.getStatus()));
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String policyTypeName(String policyType) {
        if ("LOGIN".equals(policyType)) {
            return "登录安全";
        }
        if ("RATE_LIMIT".equals(policyType)) {
            return "接口限流";
        }
        if ("RISK".equals(policyType)) {
            return "风险处理";
        }
        if ("AUDIT".equals(policyType)) {
            return "审计保留";
        }
        return "未知";
    }

    private static String policyLevelName(Integer policyLevel) {
        if (policyLevel == null) {
            return "未知";
        }
        if (policyLevel == 1) {
            return "低";
        }
        if (policyLevel == 2) {
            return "中";
        }
        if (policyLevel == 3) {
            return "高";
        }
        return "未知";
    }

    private static String policyUnitName(String policyUnit) {
        if ("COUNT".equals(policyUnit)) {
            return "次";
        }
        if ("SECOND".equals(policyUnit)) {
            return "秒";
        }
        if ("DAY".equals(policyUnit)) {
            return "天";
        }
        if ("LEVEL".equals(policyUnit)) {
            return "等级";
        }
        return policyUnit == null ? "" : policyUnit;
    }

    private static String effectiveScopeName(String effectiveScope) {
        if ("GLOBAL".equals(effectiveScope)) {
            return "全局";
        }
        if ("USER".equals(effectiveScope)) {
            return "用户";
        }
        if ("IP".equals(effectiveScope)) {
            return "客户端 IP";
        }
        if ("API".equals(effectiveScope)) {
            return "接口";
        }
        return "未知";
    }

    private static String enabledName(Integer enabled) {
        if (enabled == null) {
            return "未知";
        }
        return enabled == 1 ? "启用" : "停用";
    }

    private static String statusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        return status == 0 ? "正常" : "禁用";
    }
}
