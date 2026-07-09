package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.entity.IamRateLimitRule;
import cn.sxu.enterprise.module.iam.mapper.IamRateLimitRuleMapper;
import cn.sxu.enterprise.module.iam.service.IamRateLimitRuleService;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageQuery;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageVO;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateRequest;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class IamRateLimitRuleServiceImpl implements IamRateLimitRuleService {

    private final IamRateLimitRuleMapper iamRateLimitRuleMapper;

    @Override
    public PageResult<IamRateLimitRulePageVO> pageRateLimitRules(IamRateLimitRulePageQuery query) {
        IamRateLimitRulePageQuery safeQuery = query == null ? new IamRateLimitRulePageQuery() : query;

        long pageNo = safeQuery.getPageNo() == null || safeQuery.getPageNo() < 1 ? 1L : safeQuery.getPageNo();
        long pageSize = safeQuery.getPageSize() == null || safeQuery.getPageSize() < 1 ? 10L : safeQuery.getPageSize();
        pageSize = Math.min(pageSize, 100L);

        LambdaQueryWrapper<IamRateLimitRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(safeQuery.getRuleCode()), IamRateLimitRule::getRuleCode, safeQuery.getRuleCode());
        wrapper.like(StringUtils.hasText(safeQuery.getRuleName()), IamRateLimitRule::getRuleName, safeQuery.getRuleName());
        wrapper.like(StringUtils.hasText(safeQuery.getRequestUri()), IamRateLimitRule::getRequestUri, safeQuery.getRequestUri());
        wrapper.eq(StringUtils.hasText(safeQuery.getRequestMethod()), IamRateLimitRule::getRequestMethod, safeQuery.getRequestMethod());
        wrapper.eq(StringUtils.hasText(safeQuery.getLimitDimension()), IamRateLimitRule::getLimitDimension, safeQuery.getLimitDimension());
        wrapper.eq(safeQuery.getEnabled() != null, IamRateLimitRule::getEnabled, safeQuery.getEnabled());
        wrapper.ge(safeQuery.getBeginTime() != null, IamRateLimitRule::getCreateTime, safeQuery.getBeginTime());
        wrapper.le(safeQuery.getEndTime() != null, IamRateLimitRule::getCreateTime, safeQuery.getEndTime());
        wrapper.orderByDesc(IamRateLimitRule::getCreateTime);
        wrapper.orderByDesc(IamRateLimitRule::getId);

        Page<IamRateLimitRule> page = iamRateLimitRuleMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<IamRateLimitRulePageVO> records = page.getRecords()
            .stream()
            .map(IamRateLimitRulePageVO::fromEntity)
            .collect(Collectors.toList());

        return PageResult.of(pageNo, pageSize, page.getTotal(), page.getPages(), records);
    }

    @Override
    public Boolean enableRule(Long id) {
        IamRateLimitRule rule = getExistingRule(id);
        rule.setEnabled(1);
        rule.setUpdateBy("admin");
        rule.setUpdateTime(LocalDateTime.now());
        return iamRateLimitRuleMapper.updateById(rule) > 0;
    }

    @Override
    public Boolean disableRule(Long id) {
        IamRateLimitRule rule = getExistingRule(id);
        rule.setEnabled(0);
        rule.setUpdateBy("admin");
        rule.setUpdateTime(LocalDateTime.now());
        return iamRateLimitRuleMapper.updateById(rule) > 0;
    }

    @Override
    public IamRateLimitRuleSimulateVO simulate(IamRateLimitRuleSimulateRequest request) {
        if (request == null || !StringUtils.hasText(request.getRequestUri())) {
            throw new BusinessException(500, "requestUri 不能为空");
        }
        if (!StringUtils.hasText(request.getRequestMethod())) {
            throw new BusinessException(500, "requestMethod 不能为空");
        }

        String requestUri = request.getRequestUri().trim();
        String requestMethod = request.getRequestMethod().trim().toUpperCase();
        int currentRequests = request.getCurrentRequests() == null || request.getCurrentRequests() < 0
            ? 0
            : request.getCurrentRequests();

        LambdaQueryWrapper<IamRateLimitRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IamRateLimitRule::getEnabled, 1);
        wrapper.eq(IamRateLimitRule::getStatus, 0);
        wrapper.eq(IamRateLimitRule::getRequestUri, requestUri);
        wrapper.eq(IamRateLimitRule::getRequestMethod, requestMethod);
        wrapper.orderByAsc(IamRateLimitRule::getId);

        List<IamRateLimitRule> matchedRules = iamRateLimitRuleMapper.selectList(wrapper);
        if (matchedRules == null || matchedRules.isEmpty()) {
            IamRateLimitRuleSimulateVO vo = new IamRateLimitRuleSimulateVO();
            vo.setMatched(false);
            vo.setAllowed(true);
            vo.setHitRuleCount(0);
            vo.setRequestUri(requestUri);
            vo.setRequestMethod(requestMethod);
            vo.setCurrentRequests(currentRequests);
            vo.setRemainRequests(null);
            vo.setMessage("未命中启用的接口限流规则，本次模拟允许通过。");
            return vo;
        }

        IamRateLimitRule rule = matchedRules.get(0);
        int maxRequests = rule.getMaxRequests() == null ? 0 : rule.getMaxRequests();
        int remainRequests = Math.max(maxRequests - currentRequests, 0);
        boolean allowed = currentRequests < maxRequests;

        IamRateLimitRuleSimulateVO vo = new IamRateLimitRuleSimulateVO();
        vo.setMatched(true);
        vo.setAllowed(allowed);
        vo.setHitRuleCount(matchedRules.size());
        vo.setRuleId(rule.getId());
        vo.setRuleCode(rule.getRuleCode());
        vo.setRuleName(rule.getRuleName());
        vo.setRequestUri(rule.getRequestUri());
        vo.setRequestMethod(rule.getRequestMethod());
        vo.setLimitDimension(rule.getLimitDimension());
        vo.setLimitDimensionName(limitDimensionName(rule.getLimitDimension()));
        vo.setLimitWindowSeconds(rule.getLimitWindowSeconds());
        vo.setMaxRequests(maxRequests);
        vo.setCurrentRequests(currentRequests);
        vo.setRemainRequests(remainRequests);
        vo.setMessage(buildMessage(allowed, rule, currentRequests, remainRequests));
        return vo;
    }

    private IamRateLimitRule getExistingRule(Long id) {
        if (id == null) {
            throw new BusinessException(500, "规则 ID 不能为空");
        }
        IamRateLimitRule rule = iamRateLimitRuleMapper.selectById(id);
        if (rule == null || Objects.equals(rule.getDeleted(), 1)) {
            throw new BusinessException(500, "限流规则不存在或已删除");
        }
        return rule;
    }

    private String buildMessage(boolean allowed, IamRateLimitRule rule, int currentRequests, int remainRequests) {
        if (allowed) {
            return "命中限流规则 " + rule.getRuleCode()
                + "，当前窗口请求数 " + currentRequests
                + "，剩余额度 " + remainRequests
                + "，本次模拟允许通过。";
        }
        return "命中限流规则 " + rule.getRuleCode()
            + "，当前窗口请求数已达到或超过最大请求次数 "
            + rule.getMaxRequests()
            + "，本次模拟将被限制。";
    }

    private String limitDimensionName(String value) {
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
}
