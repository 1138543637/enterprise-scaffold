package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsAlertGenerateRequest;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertEvent;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertRule;
import cn.sxu.enterprise.module.aiops.entity.AiopsMetricData;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertEventMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertRuleMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsMetricDataMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsAlertEventService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AiopsAlertEventServiceImpl implements AiopsAlertEventService {
    private static final DateTimeFormatter CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final AiopsAlertEventMapper aiopsAlertEventMapper;
    private final AiopsAlertRuleMapper aiopsAlertRuleMapper;
    private final AiopsMetricDataMapper aiopsMetricDataMapper;
    public AiopsAlertEventServiceImpl(AiopsAlertEventMapper aiopsAlertEventMapper, AiopsAlertRuleMapper aiopsAlertRuleMapper, AiopsMetricDataMapper aiopsMetricDataMapper) {
        this.aiopsAlertEventMapper = aiopsAlertEventMapper;
        this.aiopsAlertRuleMapper = aiopsAlertRuleMapper;
        this.aiopsMetricDataMapper = aiopsMetricDataMapper;
    }
    @Override
    public PageResult<AiopsAlertEventPageVO> pageAlertEvents(AiopsAlertEventPageQuery query) {
        if (query == null) query = new AiopsAlertEventPageQuery();
        Page<AiopsAlertEvent> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<AiopsAlertEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getEventCode()), AiopsAlertEvent::getEventCode, query.getEventCode())
                .like(StringUtils.hasText(query.getRuleCode()), AiopsAlertEvent::getRuleCode, query.getRuleCode())
                .like(StringUtils.hasText(query.getResourceCode()), AiopsAlertEvent::getResourceCode, query.getResourceCode())
                .like(StringUtils.hasText(query.getResourceName()), AiopsAlertEvent::getResourceName, query.getResourceName())
                .eq(StringUtils.hasText(query.getResourceType()), AiopsAlertEvent::getResourceType, query.getResourceType())
                .like(StringUtils.hasText(query.getIpAddr()), AiopsAlertEvent::getIpAddr, query.getIpAddr())
                .like(StringUtils.hasText(query.getMetricCode()), AiopsAlertEvent::getMetricCode, query.getMetricCode())
                .eq(StringUtils.hasText(query.getMetricType()), AiopsAlertEvent::getMetricType, query.getMetricType())
                .eq(query.getAlertLevel() != null, AiopsAlertEvent::getAlertLevel, query.getAlertLevel())
                .eq(query.getHandleStatus() != null, AiopsAlertEvent::getHandleStatus, query.getHandleStatus())
                .eq(query.getStatus() != null, AiopsAlertEvent::getStatus, query.getStatus())
                .orderByDesc(AiopsAlertEvent::getAlertTime).orderByDesc(AiopsAlertEvent::getId);
        Page<AiopsAlertEvent> resultPage = aiopsAlertEventMapper.selectPage(page, wrapper);
        List<AiopsAlertEventPageVO> records = resultPage.getRecords().stream().map(AiopsAlertEventPageVO::fromEntity).toList();
        return PageResult.of(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages(), records);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AiopsAlertEventPageVO> generate(AiopsAlertGenerateRequest request) {
        if (request == null) request = new AiopsAlertGenerateRequest();
        int limit = request.getLimit() == null ? 100 : request.getLimit();
        LambdaQueryWrapper<AiopsMetricData> dataWrapper = new LambdaQueryWrapper<>();
        dataWrapper.eq(request.getMetricDataId() != null, AiopsMetricData::getId, request.getMetricDataId())
                .eq(StringUtils.hasText(request.getResourceType()), AiopsMetricData::getResourceType, request.getResourceType())
                .eq(StringUtils.hasText(request.getMetricType()), AiopsMetricData::getMetricType, request.getMetricType())
                .orderByDesc(AiopsMetricData::getCollectTime).orderByDesc(AiopsMetricData::getId);
        List<AiopsMetricData> metricDataList = aiopsMetricDataMapper.selectPage(new Page<>(1, limit), dataWrapper).getRecords();
        List<AiopsAlertRule> rules = aiopsAlertRuleMapper.selectList(new LambdaQueryWrapper<AiopsAlertRule>().eq(AiopsAlertRule::getStatus, 0));
        List<AiopsAlertEvent> inserted = metricDataList.stream()
                .flatMap(data -> rules.stream().filter(rule -> matchRule(data, rule)).filter(rule -> triggered(data.getMetricValue(), rule.getCompareOperator(), rule.getThresholdValue())).filter(rule -> notGenerated(rule.getId(), data.getId())).map(rule -> buildEvent(data, rule)))
                .peek(aiopsAlertEventMapper::insert).toList();
        return inserted.stream().map(AiopsAlertEventPageVO::fromEntity).toList();
    }
    private boolean matchRule(AiopsMetricData data, AiopsAlertRule rule) { return rule.getResourceType().equals(data.getResourceType()) && rule.getMetricType().equals(data.getMetricType()); }
    private boolean triggered(BigDecimal value, String op, BigDecimal threshold) {
        if (value == null || op == null || threshold == null) return false;
        int c = value.compareTo(threshold);
        return switch (op) { case "GT" -> c > 0; case "GE" -> c >= 0; case "LT" -> c < 0; case "LE" -> c <= 0; case "EQ" -> c == 0; default -> false; };
    }
    private boolean notGenerated(Long ruleId, Long metricDataId) {
        Long count = aiopsAlertEventMapper.selectCount(new LambdaQueryWrapper<AiopsAlertEvent>().eq(AiopsAlertEvent::getRuleId, ruleId).eq(AiopsAlertEvent::getMetricDataId, metricDataId));
        return count == null || count == 0;
    }
    private AiopsAlertEvent buildEvent(AiopsMetricData data, AiopsAlertRule rule) {
        LocalDateTime now = LocalDateTime.now();
        AiopsAlertEvent e = new AiopsAlertEvent();
        e.setEventCode("AIOPS-EVT-" + now.format(CODE_TIME_FORMATTER) + "-" + ThreadLocalRandom.current().nextInt(1000, 10000));
        e.setRuleId(rule.getId()); e.setRuleCode(rule.getRuleCode()); e.setRuleName(rule.getRuleName());
        e.setMetricDataId(data.getId()); e.setResourceId(data.getResourceId()); e.setResourceCode(data.getResourceCode()); e.setResourceName(data.getResourceName()); e.setResourceType(data.getResourceType()); e.setIpAddr(data.getIpAddr());
        e.setMetricCode(data.getMetricCode()); e.setMetricName(data.getMetricName()); e.setMetricType(data.getMetricType()); e.setMetricValue(data.getMetricValue());
        e.setThresholdValue(rule.getThresholdValue()); e.setCompareOperator(rule.getCompareOperator()); e.setAlertLevel(rule.getAlertLevel()); e.setAlertTitle(rule.getAlertTitle()); e.setAlertContent(rule.getAlertContent());
        e.setAlertTime(data.getCollectTime() == null ? now : data.getCollectTime()); e.setHandleStatus(0); e.setStatus(0); e.setCreateBy("system"); e.setCreateTime(now); e.setDeleted(0); e.setRemark("A2-04 根据指标数据自动生成");
        return e;
    }
}
