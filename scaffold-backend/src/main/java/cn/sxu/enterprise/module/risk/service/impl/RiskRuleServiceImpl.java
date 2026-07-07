package cn.sxu.enterprise.module.risk.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.entity.RiskRule;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleMapper;
import cn.sxu.enterprise.module.risk.service.RiskRuleService;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RiskRuleServiceImpl implements RiskRuleService {

    @Resource
    private RiskRuleMapper riskRuleMapper;

    @Override
    public PageResult<RiskRulePageVO> pageRules(RiskRulePageQuery query) {
        Long pageNo = normalizePageNo(query == null ? null : query.getPageNo());
        Long pageSize = normalizePageSize(query == null ? null : query.getPageSize());

        LambdaQueryWrapper<RiskRule> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            wrapper.like(StringUtils.hasText(query.getRuleCode()), RiskRule::getRuleCode, query.getRuleCode())
                    .like(StringUtils.hasText(query.getRuleName()), RiskRule::getRuleName, query.getRuleName())
                    .eq(StringUtils.hasText(query.getRuleType()), RiskRule::getRuleType, query.getRuleType())
                    .eq(query.getRiskLevel() != null, RiskRule::getRiskLevel, query.getRiskLevel())
                    .eq(query.getStatus() != null, RiskRule::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(RiskRule::getCreateTime).orderByDesc(RiskRule::getId);

        Page<RiskRule> page = riskRuleMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<RiskRulePageVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    private RiskRulePageVO toVO(RiskRule rule) {
        RiskRulePageVO vo = new RiskRulePageVO();
        vo.setId(rule.getId());
        vo.setRuleCode(rule.getRuleCode());
        vo.setRuleName(rule.getRuleName());
        vo.setRuleType(rule.getRuleType());
        vo.setConditionType(rule.getConditionType());
        vo.setCompareOperator(rule.getCompareOperator());
        vo.setThresholdValue(rule.getThresholdValue());
        vo.setRiskLevel(rule.getRiskLevel());
        vo.setRiskScore(rule.getRiskScore());
        vo.setRuleContent(rule.getRuleContent());
        vo.setStatus(rule.getStatus());
        vo.setCreateTime(rule.getCreateTime());
        vo.setRemark(rule.getRemark());
        return vo;
    }

    private Long normalizePageNo(Long pageNo) {
        return pageNo == null || pageNo < 1 ? 1L : pageNo;
    }

    private Long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 100L);
    }
}
