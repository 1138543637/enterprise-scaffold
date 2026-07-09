package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.module.iam.entity.IamLoginRisk;
import cn.sxu.enterprise.module.iam.entity.IamPermissionAudit;
import cn.sxu.enterprise.module.iam.mapper.IamLoginRiskMapper;
import cn.sxu.enterprise.module.iam.mapper.IamPermissionAuditMapper;
import cn.sxu.enterprise.module.iam.service.IamRiskClosureService;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskHandleRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditClosureRequest;
import cn.sxu.enterprise.module.iam.vo.IamRiskClosureSummaryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * IAM 风险闭环处理服务实现。
 */
@Service
@RequiredArgsConstructor
public class IamRiskClosureServiceImpl implements IamRiskClosureService {

    private static final int LOGIN_RISK_UNHANDLED = 0;
    private static final int LOGIN_RISK_CONFIRMED = 1;
    private static final int LOGIN_RISK_IGNORED = 2;
    private static final int LOGIN_RISK_CLOSED = 3;

    private static final String AUDIT_PENDING = "PENDING";
    private static final String AUDIT_REVIEWED = "REVIEWED";
    private static final String AUDIT_IGNORED = "IGNORED";

    private final IamLoginRiskMapper iamLoginRiskMapper;
    private final IamPermissionAuditMapper iamPermissionAuditMapper;

    @Override
    public IamRiskClosureSummaryVO getSummary() {
        IamRiskClosureSummaryVO summary = new IamRiskClosureSummaryVO();
        summary.setUnhandledLoginRiskCount(countLoginRiskByHandleStatus(LOGIN_RISK_UNHANDLED));
        summary.setConfirmedLoginRiskCount(countLoginRiskByHandleStatus(LOGIN_RISK_CONFIRMED));
        summary.setIgnoredLoginRiskCount(countLoginRiskByHandleStatus(LOGIN_RISK_IGNORED));
        summary.setClosedLoginRiskCount(countLoginRiskByHandleStatus(LOGIN_RISK_CLOSED));
        summary.setPendingAuditCount(countPermissionAuditByReviewStatus(AUDIT_PENDING));
        summary.setReviewedAuditCount(countPermissionAuditByReviewStatus(AUDIT_REVIEWED));
        summary.setIgnoredAuditCount(countPermissionAuditByReviewStatus(AUDIT_IGNORED));
        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmLoginRisk(Long id, IamLoginRiskHandleRequest request) {
        return handleLoginRisk(id, LOGIN_RISK_CONFIRMED, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean ignoreLoginRisk(Long id, IamLoginRiskHandleRequest request) {
        return handleLoginRisk(id, LOGIN_RISK_IGNORED, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closeLoginRisk(Long id, IamLoginRiskHandleRequest request) {
        return handleLoginRisk(id, LOGIN_RISK_CLOSED, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reviewPermissionAudit(Long id, IamPermissionAuditClosureRequest request) {
        return handlePermissionAudit(id, AUDIT_REVIEWED, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean ignorePermissionAudit(Long id, IamPermissionAuditClosureRequest request) {
        return handlePermissionAudit(id, AUDIT_IGNORED, request);
    }

    private Long countLoginRiskByHandleStatus(Integer handleStatus) {
        return iamLoginRiskMapper.selectCount(new LambdaQueryWrapper<IamLoginRisk>()
                .eq(IamLoginRisk::getHandleStatus, handleStatus));
    }

    private Long countPermissionAuditByReviewStatus(String reviewStatus) {
        return iamPermissionAuditMapper.selectCount(new LambdaQueryWrapper<IamPermissionAudit>()
                .eq(IamPermissionAudit::getReviewStatus, reviewStatus));
    }

    private Boolean handleLoginRisk(Long id, Integer handleStatus, IamLoginRiskHandleRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("登录风险 ID 不能为空");
        }
        IamLoginRisk exists = iamLoginRiskMapper.selectById(id);
        if (exists == null) {
            throw new IllegalArgumentException("登录风险记录不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        String handleBy = defaultText(request == null ? null : request.getHandleBy(), "system");
        String handleRemark = defaultText(request == null ? null : request.getHandleRemark(), buildLoginRiskDefaultRemark(handleStatus));

        UpdateWrapper<IamLoginRisk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .set("handle_status", handleStatus)
                .set("handle_by", handleBy)
                .set("handle_time", now)
                .set("handle_remark", handleRemark)
                .set("update_by", handleBy)
                .set("update_time", now)
                .set("remark", handleRemark);

        return iamLoginRiskMapper.update(null, updateWrapper) > 0;
    }

    private Boolean handlePermissionAudit(Long id, String reviewStatus, IamPermissionAuditClosureRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("权限审计 ID 不能为空");
        }
        IamPermissionAudit exists = iamPermissionAuditMapper.selectById(id);
        if (exists == null) {
            throw new IllegalArgumentException("权限审计记录不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        String reviewBy = defaultText(request == null ? null : request.getReviewBy(), "system");
        String remark = defaultText(request == null ? null : request.getRemark(), buildPermissionAuditDefaultRemark(reviewStatus));

        UpdateWrapper<IamPermissionAudit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .set("review_status", reviewStatus)
                .set("review_by", reviewBy)
                .set("review_time", now)
                .set("update_by", reviewBy)
                .set("update_time", now)
                .set("remark", remark);

        return iamPermissionAuditMapper.update(null, updateWrapper) > 0;
    }

    private String defaultText(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value.trim() : defaultValue;
    }

    private String buildLoginRiskDefaultRemark(Integer handleStatus) {
        if (LOGIN_RISK_CONFIRMED == handleStatus) {
            return "I5-08 风险闭环：已确认该异常登录风险";
        }
        if (LOGIN_RISK_IGNORED == handleStatus) {
            return "I5-08 风险闭环：已忽略该异常登录风险";
        }
        if (LOGIN_RISK_CLOSED == handleStatus) {
            return "I5-08 风险闭环：已关闭该异常登录风险";
        }
        return "I5-08 风险闭环：处理异常登录风险";
    }

    private String buildPermissionAuditDefaultRemark(String reviewStatus) {
        if (AUDIT_REVIEWED.equals(reviewStatus)) {
            return "I5-08 风险闭环：已复核该权限审计记录";
        }
        if (AUDIT_IGNORED.equals(reviewStatus)) {
            return "I5-08 风险闭环：已忽略该权限审计记录";
        }
        return "I5-08 风险闭环：处理权限审计记录";
    }
}
