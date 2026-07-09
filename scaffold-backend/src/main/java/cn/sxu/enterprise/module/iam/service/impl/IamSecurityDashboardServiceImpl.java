package cn.sxu.enterprise.module.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.sxu.enterprise.module.iam.entity.IamAccessLog;
import cn.sxu.enterprise.module.iam.entity.IamLoginRisk;
import cn.sxu.enterprise.module.iam.entity.IamPermissionAudit;
import cn.sxu.enterprise.module.iam.entity.IamRateLimitRule;
import cn.sxu.enterprise.module.iam.entity.IamSecurityPolicy;
import cn.sxu.enterprise.module.iam.mapper.IamAccessLogMapper;
import cn.sxu.enterprise.module.iam.mapper.IamLoginRiskMapper;
import cn.sxu.enterprise.module.iam.mapper.IamPermissionAuditMapper;
import cn.sxu.enterprise.module.iam.mapper.IamRateLimitRuleMapper;
import cn.sxu.enterprise.module.iam.mapper.IamSecurityPolicyMapper;
import cn.sxu.enterprise.module.iam.service.IamSecurityDashboardService;
import cn.sxu.enterprise.module.iam.vo.IamPolicyStatusStatVO;
import cn.sxu.enterprise.module.iam.vo.IamRecentSecurityEventVO;
import cn.sxu.enterprise.module.iam.vo.IamReviewStatusStatVO;
import cn.sxu.enterprise.module.iam.vo.IamRiskDistributionVO;
import cn.sxu.enterprise.module.iam.vo.IamSecurityDashboardSummaryVO;
import cn.sxu.enterprise.module.iam.vo.IamSecurityDashboardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IamSecurityDashboardServiceImpl implements IamSecurityDashboardService {

    private static final Integer STATUS_NORMAL = 0;
    private static final Integer ENABLED = 1;
    private static final Integer DISABLED = 0;
    private static final Integer ACCESS_FAILED = 1;
    private static final Integer HANDLE_UNPROCESSED = 0;

    private final IamAccessLogMapper iamAccessLogMapper;
    private final IamLoginRiskMapper iamLoginRiskMapper;
    private final IamRateLimitRuleMapper iamRateLimitRuleMapper;
    private final IamSecurityPolicyMapper iamSecurityPolicyMapper;
    private final IamPermissionAuditMapper iamPermissionAuditMapper;

    @Override
    public IamSecurityDashboardVO getDashboard() {
        IamSecurityDashboardVO dashboard = new IamSecurityDashboardVO();
        dashboard.setSummary(buildSummary());
        dashboard.setRiskDistributions(buildRiskDistributions());
        dashboard.setReviewStatusStats(buildReviewStatusStats());
        dashboard.setPolicyStatusStats(buildPolicyStatusStats());
        dashboard.setRecentEvents(buildRecentEvents());
        return dashboard;
    }

    private IamSecurityDashboardSummaryVO buildSummary() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrowStart = todayStart.plusDays(1);

        IamSecurityDashboardSummaryVO summary = new IamSecurityDashboardSummaryVO();
        summary.setTodayAccessCount(count(iamAccessLogMapper.selectCount(
                new LambdaQueryWrapper<IamAccessLog>()
                        .ge(IamAccessLog::getAccessTime, todayStart)
                        .lt(IamAccessLog::getAccessTime, tomorrowStart)
        )));
        summary.setTodayFailedAccessCount(count(iamAccessLogMapper.selectCount(
                new LambdaQueryWrapper<IamAccessLog>()
                        .eq(IamAccessLog::getAccessStatus, ACCESS_FAILED)
                        .ge(IamAccessLog::getAccessTime, todayStart)
                        .lt(IamAccessLog::getAccessTime, tomorrowStart)
        )));
        summary.setUnhandledLoginRiskCount(count(iamLoginRiskMapper.selectCount(
                new LambdaQueryWrapper<IamLoginRisk>()
                        .eq(IamLoginRisk::getHandleStatus, HANDLE_UNPROCESSED)
        )));
        summary.setHighRiskPermissionAuditCount(count(iamPermissionAuditMapper.selectCount(
                new LambdaQueryWrapper<IamPermissionAudit>()
                        .eq(IamPermissionAudit::getRiskLevel, "HIGH")
        )));
        summary.setPendingAuditCount(count(iamPermissionAuditMapper.selectCount(
                new LambdaQueryWrapper<IamPermissionAudit>()
                        .eq(IamPermissionAudit::getReviewStatus, "PENDING")
        )));
        summary.setEnabledSecurityPolicyCount(count(iamSecurityPolicyMapper.selectCount(
                new LambdaQueryWrapper<IamSecurityPolicy>()
                        .eq(IamSecurityPolicy::getEnabled, ENABLED)
                        .eq(IamSecurityPolicy::getStatus, STATUS_NORMAL)
        )));
        summary.setEnabledRateLimitRuleCount(count(iamRateLimitRuleMapper.selectCount(
                new LambdaQueryWrapper<IamRateLimitRule>()
                        .eq(IamRateLimitRule::getEnabled, ENABLED)
                        .eq(IamRateLimitRule::getStatus, STATUS_NORMAL)
        )));
        return summary;
    }

    private List<IamRiskDistributionVO> buildRiskDistributions() {
        List<IamRiskDistributionVO> list = new ArrayList<>();
        list.add(buildRiskDistribution("LOW", "低风险", 1));
        list.add(buildRiskDistribution("MEDIUM", "中风险", 2));
        list.add(buildRiskDistribution("HIGH", "高风险", 3));
        return list;
    }

    private IamRiskDistributionVO buildRiskDistribution(String auditRiskLevel, String riskLevelName, Integer loginRiskLevel) {
        Long loginRiskCount = count(iamLoginRiskMapper.selectCount(
                new LambdaQueryWrapper<IamLoginRisk>()
                        .eq(IamLoginRisk::getRiskLevel, loginRiskLevel)
        ));
        Long permissionAuditCount = count(iamPermissionAuditMapper.selectCount(
                new LambdaQueryWrapper<IamPermissionAudit>()
                        .eq(IamPermissionAudit::getRiskLevel, auditRiskLevel)
        ));

        IamRiskDistributionVO vo = new IamRiskDistributionVO();
        vo.setRiskLevel(auditRiskLevel);
        vo.setRiskLevelName(riskLevelName);
        vo.setLoginRiskCount(loginRiskCount);
        vo.setPermissionAuditCount(permissionAuditCount);
        vo.setTotalCount(loginRiskCount + permissionAuditCount);
        return vo;
    }

    private List<IamReviewStatusStatVO> buildReviewStatusStats() {
        List<IamReviewStatusStatVO> list = new ArrayList<>();
        list.add(buildReviewStatusStat("PENDING", "待复核"));
        list.add(buildReviewStatusStat("REVIEWED", "已复核"));
        list.add(buildReviewStatusStat("IGNORED", "已忽略"));
        return list;
    }

    private IamReviewStatusStatVO buildReviewStatusStat(String reviewStatus, String reviewStatusName) {
        IamReviewStatusStatVO vo = new IamReviewStatusStatVO();
        vo.setReviewStatus(reviewStatus);
        vo.setReviewStatusName(reviewStatusName);
        vo.setTotalCount(count(iamPermissionAuditMapper.selectCount(
                new LambdaQueryWrapper<IamPermissionAudit>()
                        .eq(IamPermissionAudit::getReviewStatus, reviewStatus)
        )));
        return vo;
    }

    private List<IamPolicyStatusStatVO> buildPolicyStatusStats() {
        List<IamPolicyStatusStatVO> list = new ArrayList<>();
        list.add(buildPolicyStatusStat(ENABLED, "启用"));
        list.add(buildPolicyStatusStat(DISABLED, "停用"));
        return list;
    }

    private IamPolicyStatusStatVO buildPolicyStatusStat(Integer enabled, String enabledName) {
        Long securityPolicyCount = count(iamSecurityPolicyMapper.selectCount(
                new LambdaQueryWrapper<IamSecurityPolicy>()
                        .eq(IamSecurityPolicy::getEnabled, enabled)
        ));
        Long rateLimitRuleCount = count(iamRateLimitRuleMapper.selectCount(
                new LambdaQueryWrapper<IamRateLimitRule>()
                        .eq(IamRateLimitRule::getEnabled, enabled)
        ));

        IamPolicyStatusStatVO vo = new IamPolicyStatusStatVO();
        vo.setEnabled(enabled);
        vo.setEnabledName(enabledName);
        vo.setSecurityPolicyCount(securityPolicyCount);
        vo.setRateLimitRuleCount(rateLimitRuleCount);
        vo.setTotalCount(securityPolicyCount + rateLimitRuleCount);
        return vo;
    }

    private List<IamRecentSecurityEventVO> buildRecentEvents() {
        List<IamRecentSecurityEventVO> events = new ArrayList<>();

        List<IamLoginRisk> loginRisks = iamLoginRiskMapper.selectList(
                new LambdaQueryWrapper<IamLoginRisk>()
                        .orderByDesc(IamLoginRisk::getDetectTime)
                        .orderByDesc(IamLoginRisk::getId)
                        .last("LIMIT 5")
        );
        for (IamLoginRisk risk : loginRisks) {
            events.add(fromLoginRisk(risk));
        }

        List<IamPermissionAudit> permissionAudits = iamPermissionAuditMapper.selectList(
                new LambdaQueryWrapper<IamPermissionAudit>()
                        .orderByDesc(IamPermissionAudit::getCreateTime)
                        .orderByDesc(IamPermissionAudit::getId)
                        .last("LIMIT 5")
        );
        for (IamPermissionAudit audit : permissionAudits) {
            events.add(fromPermissionAudit(audit));
        }

        events.sort(Comparator.comparing(IamRecentSecurityEventVO::getEventTime,
                Comparator.nullsLast(Comparator.reverseOrder())));
        if (events.size() > 10) {
            return new ArrayList<>(events.subList(0, 10));
        }
        return events;
    }

    private IamRecentSecurityEventVO fromLoginRisk(IamLoginRisk risk) {
        IamRecentSecurityEventVO vo = new IamRecentSecurityEventVO();
        vo.setEventType("LOGIN_RISK");
        vo.setEventTypeName("异常登录");
        vo.setEventCode(risk.getRiskCode());
        vo.setEventTitle(riskTypeName(risk.getRiskType()));
        vo.setEventDetail("用户 " + safeText(risk.getUsername()) + " 在 IP " + safeText(risk.getClientIp())
                + " 触发登录风险，失败次数 " + safeNumber(risk.getFailCount()) + " 次");
        vo.setRiskLevel(String.valueOf(risk.getRiskLevel()));
        vo.setRiskLevelName(loginRiskLevelName(risk.getRiskLevel()));
        vo.setStatusText(handleStatusName(risk.getHandleStatus()));
        vo.setOperatorName(risk.getUsername());
        vo.setRequestIp(risk.getClientIp());
        vo.setEventTime(risk.getDetectTime());
        return vo;
    }

    private IamRecentSecurityEventVO fromPermissionAudit(IamPermissionAudit audit) {
        IamRecentSecurityEventVO vo = new IamRecentSecurityEventVO();
        vo.setEventType("PERMISSION_AUDIT");
        vo.setEventTypeName("权限审计");
        vo.setEventCode(audit.getAuditCode());
        vo.setEventTitle(auditTypeName(audit.getAuditType()));
        vo.setEventDetail(safeText(audit.getOperatorName()) + " 对 " + targetTypeName(audit.getTargetType())
                + "「" + safeText(audit.getTargetName()) + "」执行 " + changeActionName(audit.getChangeAction()));
        vo.setRiskLevel(audit.getRiskLevel());
        vo.setRiskLevelName(permissionRiskLevelName(audit.getRiskLevel()));
        vo.setStatusText(reviewStatusName(audit.getReviewStatus()));
        vo.setOperatorName(audit.getOperatorName());
        vo.setRequestIp(audit.getRequestIp());
        vo.setEventTime(audit.getCreateTime());
        return vo;
    }

    private Long count(Long value) {
        return value == null ? 0L : value;
    }

    private String safeText(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value;
    }

    private Integer safeNumber(Integer value) {
        return value == null ? 0 : value;
    }

    private String riskTypeName(String riskType) {
        if ("LOGIN_FAILED".equals(riskType)) {
            return "登录失败";
        }
        if ("SHORT_TIME_FAILED".equals(riskType)) {
            return "短时间多次失败";
        }
        if ("ABNORMAL_IP".equals(riskType)) {
            return "异常 IP";
        }
        return "未知登录风险";
    }

    private String loginRiskLevelName(Integer riskLevel) {
        if (Integer.valueOf(1).equals(riskLevel)) {
            return "低风险";
        }
        if (Integer.valueOf(2).equals(riskLevel)) {
            return "中风险";
        }
        if (Integer.valueOf(3).equals(riskLevel)) {
            return "高风险";
        }
        return "未知风险";
    }

    private String handleStatusName(Integer handleStatus) {
        if (Integer.valueOf(0).equals(handleStatus)) {
            return "未处理";
        }
        if (Integer.valueOf(1).equals(handleStatus)) {
            return "已确认";
        }
        if (Integer.valueOf(2).equals(handleStatus)) {
            return "已忽略";
        }
        return "未知状态";
    }

    private String auditTypeName(String auditType) {
        if ("USER_ROLE".equals(auditType)) {
            return "用户角色变更";
        }
        if ("ROLE_MENU".equals(auditType)) {
            return "角色菜单变更";
        }
        if ("USER_STATUS".equals(auditType)) {
            return "用户状态变更";
        }
        if ("MENU_PERMISSION".equals(auditType)) {
            return "菜单权限变更";
        }
        if ("DATA_SCOPE".equals(auditType)) {
            return "数据范围变更";
        }
        return "权限审计事件";
    }

    private String targetTypeName(String targetType) {
        if ("USER".equals(targetType)) {
            return "用户";
        }
        if ("ROLE".equals(targetType)) {
            return "角色";
        }
        if ("MENU".equals(targetType)) {
            return "菜单";
        }
        if ("API".equals(targetType)) {
            return "接口";
        }
        return "对象";
    }

    private String changeActionName(String changeAction) {
        if ("GRANT".equals(changeAction)) {
            return "授权";
        }
        if ("REVOKE".equals(changeAction)) {
            return "撤销";
        }
        if ("UPDATE".equals(changeAction)) {
            return "修改";
        }
        if ("ENABLE".equals(changeAction)) {
            return "启用";
        }
        if ("DISABLE".equals(changeAction)) {
            return "停用";
        }
        return "变更";
    }

    private String permissionRiskLevelName(String riskLevel) {
        if ("LOW".equals(riskLevel)) {
            return "低风险";
        }
        if ("MEDIUM".equals(riskLevel)) {
            return "中风险";
        }
        if ("HIGH".equals(riskLevel)) {
            return "高风险";
        }
        return "未知风险";
    }

    private String reviewStatusName(String reviewStatus) {
        if ("PENDING".equals(reviewStatus)) {
            return "待复核";
        }
        if ("REVIEWED".equals(reviewStatus)) {
            return "已复核";
        }
        if ("IGNORED".equals(reviewStatus)) {
            return "已忽略";
        }
        return "未知状态";
    }
}
