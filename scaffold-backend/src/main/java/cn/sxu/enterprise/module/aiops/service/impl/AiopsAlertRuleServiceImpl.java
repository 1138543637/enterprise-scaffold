package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertRule;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertRuleMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsAlertRuleService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class AiopsAlertRuleServiceImpl implements AiopsAlertRuleService {
    private final AiopsAlertRuleMapper aiopsAlertRuleMapper;
    public AiopsAlertRuleServiceImpl(AiopsAlertRuleMapper aiopsAlertRuleMapper) { this.aiopsAlertRuleMapper = aiopsAlertRuleMapper; }
    @Override
    public PageResult<AiopsAlertRulePageVO> pageAlertRules(AiopsAlertRulePageQuery query) {
        if (query == null) query = new AiopsAlertRulePageQuery();
        Page<AiopsAlertRule> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<AiopsAlertRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRuleCode()), AiopsAlertRule::getRuleCode, query.getRuleCode())
                .like(StringUtils.hasText(query.getRuleName()), AiopsAlertRule::getRuleName, query.getRuleName())
                .eq(StringUtils.hasText(query.getResourceType()), AiopsAlertRule::getResourceType, query.getResourceType())
                .eq(StringUtils.hasText(query.getMetricType()), AiopsAlertRule::getMetricType, query.getMetricType())
                .eq(query.getAlertLevel() != null, AiopsAlertRule::getAlertLevel, query.getAlertLevel())
                .eq(query.getStatus() != null, AiopsAlertRule::getStatus, query.getStatus())
                .orderByDesc(AiopsAlertRule::getCreateTime).orderByDesc(AiopsAlertRule::getId);
        Page<AiopsAlertRule> resultPage = aiopsAlertRuleMapper.selectPage(page, wrapper);
        List<AiopsAlertRulePageVO> records = resultPage.getRecords().stream().map(AiopsAlertRulePageVO::fromEntity).toList();
        return PageResult.of(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages(), records);
    }
}
