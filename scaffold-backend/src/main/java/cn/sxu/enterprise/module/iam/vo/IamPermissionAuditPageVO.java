package cn.sxu.enterprise.module.iam.vo;

import cn.sxu.enterprise.module.iam.entity.IamPermissionAudit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IamPermissionAuditPageVO {

    private Long id;

    private String auditCode;

    private String auditType;

    private String auditTypeName;

    private String targetType;

    private String targetTypeName;

    private Long targetId;

    private String targetName;

    private String changeAction;

    private String changeActionName;

    private String beforeValue;

    private String afterValue;

    private String riskLevel;

    private String riskLevelName;

    private String reviewStatus;

    private String reviewStatusName;

    private String reviewBy;

    private LocalDateTime reviewTime;

    private String requestIp;

    private String operatorName;

    private Integer status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String remark;

    public static IamPermissionAuditPageVO fromEntity(IamPermissionAudit entity) {
        IamPermissionAuditPageVO vo = new IamPermissionAuditPageVO();
        if (entity == null) {
            return vo;
        }
        vo.setId(entity.getId());
        vo.setAuditCode(entity.getAuditCode());
        vo.setAuditType(entity.getAuditType());
        vo.setAuditTypeName(auditTypeName(entity.getAuditType()));
        vo.setTargetType(entity.getTargetType());
        vo.setTargetTypeName(targetTypeName(entity.getTargetType()));
        vo.setTargetId(entity.getTargetId());
        vo.setTargetName(entity.getTargetName());
        vo.setChangeAction(entity.getChangeAction());
        vo.setChangeActionName(changeActionName(entity.getChangeAction()));
        vo.setBeforeValue(entity.getBeforeValue());
        vo.setAfterValue(entity.getAfterValue());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setRiskLevelName(riskLevelName(entity.getRiskLevel()));
        vo.setReviewStatus(entity.getReviewStatus());
        vo.setReviewStatusName(reviewStatusName(entity.getReviewStatus()));
        vo.setReviewBy(entity.getReviewBy());
        vo.setReviewTime(entity.getReviewTime());
        vo.setRequestIp(entity.getRequestIp());
        vo.setOperatorName(entity.getOperatorName());
        vo.setStatus(entity.getStatus());
        vo.setStatusName(statusName(entity.getStatus()));
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String auditTypeName(String value) {
        if ("USER_ROLE".equals(value)) {
            return "用户角色变更";
        }
        if ("ROLE_MENU".equals(value)) {
            return "角色菜单变更";
        }
        if ("USER_STATUS".equals(value)) {
            return "用户状态变更";
        }
        if ("MENU_PERMISSION".equals(value)) {
            return "菜单权限变更";
        }
        if ("DATA_SCOPE".equals(value)) {
            return "数据范围变更";
        }
        return value;
    }

    private static String targetTypeName(String value) {
        if ("USER".equals(value)) {
            return "用户";
        }
        if ("ROLE".equals(value)) {
            return "角色";
        }
        if ("MENU".equals(value)) {
            return "菜单";
        }
        if ("API".equals(value)) {
            return "接口";
        }
        return value;
    }

    private static String changeActionName(String value) {
        if ("GRANT".equals(value)) {
            return "授权";
        }
        if ("REVOKE".equals(value)) {
            return "撤销";
        }
        if ("UPDATE".equals(value)) {
            return "修改";
        }
        if ("ENABLE".equals(value)) {
            return "启用";
        }
        if ("DISABLE".equals(value)) {
            return "停用";
        }
        return value;
    }

    private static String riskLevelName(String value) {
        if ("LOW".equals(value)) {
            return "低";
        }
        if ("MEDIUM".equals(value)) {
            return "中";
        }
        if ("HIGH".equals(value)) {
            return "高";
        }
        return value;
    }

    private static String reviewStatusName(String value) {
        if ("PENDING".equals(value)) {
            return "待复核";
        }
        if ("REVIEWED".equals(value)) {
            return "已复核";
        }
        if ("IGNORED".equals(value)) {
            return "已忽略";
        }
        return value;
    }

    private static String statusName(Integer value) {
        if (Integer.valueOf(0).equals(value)) {
            return "正常";
        }
        if (Integer.valueOf(1).equals(value)) {
            return "禁用";
        }
        return value == null ? null : String.valueOf(value);
    }
}
