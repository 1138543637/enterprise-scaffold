package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.entity.IamSecurityPolicy;
import cn.sxu.enterprise.module.iam.mapper.IamSecurityPolicyMapper;
import cn.sxu.enterprise.module.iam.service.IamSecurityPolicyService;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageVO;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyUpdateRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IamSecurityPolicyServiceImpl implements IamSecurityPolicyService {

    private final IamSecurityPolicyMapper iamSecurityPolicyMapper;

    @Override
    public PageResult<IamSecurityPolicyPageVO> pageSecurityPolicies(IamSecurityPolicyPageQuery query) {
        IamSecurityPolicyPageQuery safeQuery = query == null ? new IamSecurityPolicyPageQuery() : query;
        long pageNo = normalizePageNo(safeQuery.getPageNo());
        long pageSize = normalizePageSize(safeQuery.getPageSize());

        LambdaQueryWrapper<IamSecurityPolicy> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(safeQuery.getPolicyCode()), IamSecurityPolicy::getPolicyCode, safeQuery.getPolicyCode());
        wrapper.like(StringUtils.hasText(safeQuery.getPolicyName()), IamSecurityPolicy::getPolicyName, safeQuery.getPolicyName());
        wrapper.eq(StringUtils.hasText(safeQuery.getPolicyType()), IamSecurityPolicy::getPolicyType, safeQuery.getPolicyType());
        wrapper.eq(safeQuery.getPolicyLevel() != null, IamSecurityPolicy::getPolicyLevel, safeQuery.getPolicyLevel());
        wrapper.eq(StringUtils.hasText(safeQuery.getEffectiveScope()), IamSecurityPolicy::getEffectiveScope, safeQuery.getEffectiveScope());
        wrapper.eq(safeQuery.getEnabled() != null, IamSecurityPolicy::getEnabled, safeQuery.getEnabled());
        wrapper.eq(safeQuery.getStatus() != null, IamSecurityPolicy::getStatus, safeQuery.getStatus());
        wrapper.ge(safeQuery.getBeginTime() != null, IamSecurityPolicy::getCreateTime, safeQuery.getBeginTime());
        wrapper.le(safeQuery.getEndTime() != null, IamSecurityPolicy::getCreateTime, safeQuery.getEndTime());
        wrapper.orderByDesc(IamSecurityPolicy::getCreateTime);
        wrapper.orderByDesc(IamSecurityPolicy::getId);

        Page<IamSecurityPolicy> page = iamSecurityPolicyMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<IamSecurityPolicyPageVO> records = page.getRecords().stream()
                .map(IamSecurityPolicyPageVO::fromEntity)
                .toList();
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
    public Boolean enablePolicy(Long id) {
        IamSecurityPolicy policy = getPolicyOrThrow(id);
        policy.setEnabled(1);
        policy.setUpdateBy("system");
        policy.setUpdateTime(LocalDateTime.now());
        return iamSecurityPolicyMapper.updateById(policy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disablePolicy(Long id) {
        IamSecurityPolicy policy = getPolicyOrThrow(id);
        policy.setEnabled(0);
        policy.setUpdateBy("system");
        policy.setUpdateTime(LocalDateTime.now());
        return iamSecurityPolicyMapper.updateById(policy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamSecurityPolicyPageVO updatePolicy(Long id, IamSecurityPolicyUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("安全策略配置不能为空");
        }
        IamSecurityPolicy policy = getPolicyOrThrow(id);
        if (StringUtils.hasText(request.getPolicyValue())) {
            policy.setPolicyValue(request.getPolicyValue().trim());
        }
        if (StringUtils.hasText(request.getPolicyUnit())) {
            policy.setPolicyUnit(request.getPolicyUnit().trim().toUpperCase());
        }
        if (StringUtils.hasText(request.getEffectiveScope())) {
            policy.setEffectiveScope(request.getEffectiveScope().trim().toUpperCase());
        }
        if (request.getEnabled() != null) {
            policy.setEnabled(request.getEnabled());
        }
        if (request.getRemark() != null) {
            policy.setRemark(request.getRemark());
        }
        policy.setUpdateBy("system");
        policy.setUpdateTime(LocalDateTime.now());
        iamSecurityPolicyMapper.updateById(policy);
        return IamSecurityPolicyPageVO.fromEntity(iamSecurityPolicyMapper.selectById(id));
    }

    private IamSecurityPolicy getPolicyOrThrow(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("安全策略 ID 不能为空");
        }
        IamSecurityPolicy policy = iamSecurityPolicyMapper.selectById(id);
        if (policy == null) {
            throw new IllegalArgumentException("安全策略不存在");
        }
        return policy;
    }

    private long normalizePageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            return 1L;
        }
        return pageNo;
    }

    private long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 100L);
    }
}
