package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.entity.IamPermissionAudit;
import cn.sxu.enterprise.module.iam.mapper.IamPermissionAuditMapper;
import cn.sxu.enterprise.module.iam.service.IamPermissionAuditService;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageVO;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditReviewRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditSimulateRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IamPermissionAuditServiceImpl implements IamPermissionAuditService {

    private final IamPermissionAuditMapper iamPermissionAuditMapper;

    @Override
    public PageResult<IamPermissionAuditPageVO> pagePermissionAudits(IamPermissionAuditPageQuery query) {
        IamPermissionAuditPageQuery safeQuery = query == null ? new IamPermissionAuditPageQuery() : query;
        long pageNo = safeQuery.getPageNo() == null || safeQuery.getPageNo() < 1 ? 1L : safeQuery.getPageNo();
        long pageSize = safeQuery.getPageSize() == null || safeQuery.getPageSize() < 1 ? 10L : safeQuery.getPageSize();
        pageSize = Math.min(pageSize, 100L);

        LambdaQueryWrapper<IamPermissionAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(safeQuery.getAuditCode()), IamPermissionAudit::getAuditCode, safeQuery.getAuditCode())
                .eq(StringUtils.hasText(safeQuery.getAuditType()), IamPermissionAudit::getAuditType, safeQuery.getAuditType())
                .eq(StringUtils.hasText(safeQuery.getTargetType()), IamPermissionAudit::getTargetType, safeQuery.getTargetType())
                .like(StringUtils.hasText(safeQuery.getTargetName()), IamPermissionAudit::getTargetName, safeQuery.getTargetName())
                .eq(StringUtils.hasText(safeQuery.getChangeAction()), IamPermissionAudit::getChangeAction, safeQuery.getChangeAction())
                .eq(StringUtils.hasText(safeQuery.getRiskLevel()), IamPermissionAudit::getRiskLevel, safeQuery.getRiskLevel())
                .eq(StringUtils.hasText(safeQuery.getReviewStatus()), IamPermissionAudit::getReviewStatus, safeQuery.getReviewStatus())
                .like(StringUtils.hasText(safeQuery.getOperatorName()), IamPermissionAudit::getOperatorName, safeQuery.getOperatorName())
                .ge(safeQuery.getBeginTime() != null, IamPermissionAudit::getCreateTime, safeQuery.getBeginTime())
                .le(safeQuery.getEndTime() != null, IamPermissionAudit::getCreateTime, safeQuery.getEndTime())
                .orderByDesc(IamPermissionAudit::getCreateTime)
                .orderByDesc(IamPermissionAudit::getId);

        Page<IamPermissionAudit> page = iamPermissionAuditMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<IamPermissionAuditPageVO> records = page.getRecords()
                .stream()
                .map(IamPermissionAuditPageVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(
                Long.valueOf(page.getTotal()),
                Long.valueOf(page.getPages()),
                Long.valueOf(pageNo),
                Long.valueOf(pageSize),
                records
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamPermissionAuditPageVO simulatePermissionAudit(IamPermissionAuditSimulateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("权限审计模拟参数不能为空");
        }
        if (!StringUtils.hasText(request.getAuditType())) {
            throw new IllegalArgumentException("审计类型不能为空");
        }
        if (!StringUtils.hasText(request.getTargetType())) {
            throw new IllegalArgumentException("审计对象类型不能为空");
        }
        if (!StringUtils.hasText(request.getChangeAction())) {
            throw new IllegalArgumentException("变更动作不能为空");
        }

        LocalDateTime now = LocalDateTime.now();
        IamPermissionAudit audit = new IamPermissionAudit();
        audit.setAuditCode(buildAuditCode());
        audit.setAuditType(request.getAuditType().trim().toUpperCase(Locale.ROOT));
        audit.setTargetType(request.getTargetType().trim().toUpperCase(Locale.ROOT));
        audit.setTargetId(request.getTargetId());
        audit.setTargetName(request.getTargetName());
        audit.setChangeAction(request.getChangeAction().trim().toUpperCase(Locale.ROOT));
        audit.setBeforeValue(request.getBeforeValue());
        audit.setAfterValue(request.getAfterValue());
        audit.setRiskLevel(StringUtils.hasText(request.getRiskLevel()) ? request.getRiskLevel().trim().toUpperCase(Locale.ROOT) : "LOW");
        audit.setReviewStatus("PENDING");
        audit.setRequestIp(request.getRequestIp());
        audit.setOperatorName(StringUtils.hasText(request.getOperatorName()) ? request.getOperatorName() : "admin");
        audit.setStatus(0);
        audit.setCreateBy("system");
        audit.setCreateTime(now);
        audit.setUpdateTime(now);
        audit.setDeleted(0);
        audit.setRemark(request.getRemark());

        iamPermissionAuditMapper.insert(audit);
        return IamPermissionAuditPageVO.fromEntity(audit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamPermissionAuditPageVO reviewPermissionAudit(Long id, IamPermissionAuditReviewRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("权限审计记录ID不能为空");
        }
        if (request == null || !StringUtils.hasText(request.getReviewStatus())) {
            throw new IllegalArgumentException("复核状态不能为空");
        }

        IamPermissionAudit audit = iamPermissionAuditMapper.selectById(id);
        if (audit == null) {
            throw new IllegalArgumentException("权限审计记录不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        audit.setReviewStatus(request.getReviewStatus().trim().toUpperCase(Locale.ROOT));
        audit.setReviewBy(StringUtils.hasText(request.getReviewBy()) ? request.getReviewBy() : "admin");
        audit.setReviewTime(now);
        audit.setRemark(request.getRemark());
        audit.setUpdateBy(audit.getReviewBy());
        audit.setUpdateTime(now);

        iamPermissionAuditMapper.updateById(audit);
        return IamPermissionAuditPageVO.fromEntity(audit);
    }

    private String buildAuditCode() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
        return "PA-" + timePart + "-" + randomPart;
    }
}
