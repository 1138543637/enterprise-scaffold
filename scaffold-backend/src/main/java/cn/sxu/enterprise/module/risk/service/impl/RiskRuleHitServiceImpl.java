package cn.sxu.enterprise.module.risk.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskRuleHitGenerateRequest;
import cn.sxu.enterprise.module.risk.entity.RiskRule;
import cn.sxu.enterprise.module.risk.entity.RiskRuleHit;
import cn.sxu.enterprise.module.risk.entity.RiskTransaction;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleHitMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskTransactionMapper;
import cn.sxu.enterprise.module.risk.service.RiskRuleHitService;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RiskRuleHitServiceImpl implements RiskRuleHitService {

    private static final DateTimeFormatter HIT_CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Resource
    private RiskRuleMapper riskRuleMapper;

    @Resource
    private RiskRuleHitMapper riskRuleHitMapper;

    @Resource
    private RiskTransactionMapper riskTransactionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RiskRuleHitPageVO> generate(RiskRuleHitGenerateRequest request) {
        List<RiskRule> rules = riskRuleMapper.selectList(new LambdaQueryWrapper<RiskRule>()
                .eq(RiskRule::getStatus, 0)
                .orderByAsc(RiskRule::getId));

        if (rules.isEmpty()) {
            return List.of();
        }

        List<RiskTransaction> transactions = loadTransactions(request);
        List<RiskRuleHitPageVO> result = new ArrayList<>();

        for (RiskTransaction transaction : transactions) {
            boolean hitAnyRule = false;
            for (RiskRule rule : rules) {
                RuleMatchResult matchResult = evaluateRule(rule, transaction);
                if (!matchResult.matched()) {
                    continue;
                }
                if (existsHit(transaction.getId(), rule.getId())) {
                    continue;
                }

                RiskRuleHit hit = buildRuleHit(transaction, rule, matchResult.hitValue());
                riskRuleHitMapper.insert(hit);
                result.add(toVO(hit));
                hitAnyRule = true;
            }

            if (hitAnyRule) {
                transaction.setRiskFlag(1);
                transaction.setStatus(1);
                transaction.setUpdateTime(LocalDateTime.now());
                riskTransactionMapper.updateById(transaction);
            }
        }

        return result;
    }

    @Override
    public PageResult<RiskRuleHitPageVO> pageRuleHits(RiskRuleHitPageQuery query) {
        Long pageNo = normalizePageNo(query == null ? null : query.getPageNo());
        Long pageSize = normalizePageSize(query == null ? null : query.getPageSize());

        LambdaQueryWrapper<RiskRuleHit> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            wrapper.like(StringUtils.hasText(query.getHitCode()), RiskRuleHit::getHitCode, query.getHitCode())
                    .like(StringUtils.hasText(query.getTransactionNo()), RiskRuleHit::getTransactionNo, query.getTransactionNo())
                    .like(StringUtils.hasText(query.getAccountNo()), RiskRuleHit::getAccountNo, query.getAccountNo())
                    .like(StringUtils.hasText(query.getCustomerName()), RiskRuleHit::getCustomerName, query.getCustomerName())
                    .like(StringUtils.hasText(query.getRuleCode()), RiskRuleHit::getRuleCode, query.getRuleCode())
                    .like(StringUtils.hasText(query.getRuleName()), RiskRuleHit::getRuleName, query.getRuleName())
                    .eq(StringUtils.hasText(query.getRuleType()), RiskRuleHit::getRuleType, query.getRuleType())
                    .eq(query.getRiskLevel() != null, RiskRuleHit::getRiskLevel, query.getRiskLevel())
                    .eq(query.getStatus() != null, RiskRuleHit::getStatus, query.getStatus())
                    .ge(query.getBeginTime() != null, RiskRuleHit::getHitTime, query.getBeginTime())
                    .le(query.getEndTime() != null, RiskRuleHit::getHitTime, query.getEndTime());
        }
        wrapper.orderByDesc(RiskRuleHit::getHitTime).orderByDesc(RiskRuleHit::getId);

        Page<RiskRuleHit> page = riskRuleHitMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<RiskRuleHitPageVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    private List<RiskTransaction> loadTransactions(RiskRuleHitGenerateRequest request) {
        if (request != null && request.getTransactionId() != null) {
            RiskTransaction transaction = riskTransactionMapper.selectById(request.getTransactionId());
            return transaction == null ? List.of() : List.of(transaction);
        }

        int limit = normalizeLimit(request == null ? null : request.getLimit());
        return riskTransactionMapper.selectList(new LambdaQueryWrapper<RiskTransaction>()
                .orderByDesc(RiskTransaction::getTransactionTime)
                .orderByDesc(RiskTransaction::getId)
                .last("LIMIT " + limit));
    }

    private RuleMatchResult evaluateRule(RiskRule rule, RiskTransaction transaction) {
        if (rule == null || transaction == null || !StringUtils.hasText(rule.getRuleType())) {
            return RuleMatchResult.notMatched();
        }

        String ruleType = rule.getRuleType().toUpperCase(Locale.ROOT);
        return switch (ruleType) {
            case "AMOUNT" -> evaluateAmountRule(rule, transaction);
            case "FREQUENCY" -> evaluateFrequencyRule(rule, transaction);
            case "LOCATION" -> evaluateTextContainsRule(rule, transaction.getLocation());
            case "DEVICE" -> evaluateTextContainsRule(rule, transaction.getDeviceId());
            case "TIME" -> evaluateTimeRule(rule, transaction);
            case "BLACKLIST" -> evaluateBlacklistRule(rule, transaction);
            default -> RuleMatchResult.notMatched();
        };
    }

    private RuleMatchResult evaluateAmountRule(RiskRule rule, RiskTransaction transaction) {
        BigDecimal amount = transaction.getAmount();
        BigDecimal threshold = rule.getThresholdValue();
        if (amount == null || threshold == null) {
            return RuleMatchResult.notMatched();
        }
        boolean matched = compare(amount, threshold, rule.getCompareOperator());
        return matched ? RuleMatchResult.matched(amount.toPlainString()) : RuleMatchResult.notMatched();
    }

    private RuleMatchResult evaluateFrequencyRule(RiskRule rule, RiskTransaction transaction) {
        if (!StringUtils.hasText(transaction.getAccountNo())) {
            return RuleMatchResult.notMatched();
        }
        LocalDateTime endTime = transaction.getTransactionTime() == null ? LocalDateTime.now() : transaction.getTransactionTime();
        LocalDateTime beginTime = endTime.minusMinutes(10);
        Long count = riskTransactionMapper.selectCount(new LambdaQueryWrapper<RiskTransaction>()
                .eq(RiskTransaction::getAccountNo, transaction.getAccountNo())
                .ge(RiskTransaction::getTransactionTime, beginTime)
                .le(RiskTransaction::getTransactionTime, endTime));
        BigDecimal threshold = rule.getThresholdValue() == null ? BigDecimal.valueOf(3) : rule.getThresholdValue();
        boolean matched = BigDecimal.valueOf(count).compareTo(threshold) >= 0;
        return matched ? RuleMatchResult.matched(String.valueOf(count)) : RuleMatchResult.notMatched();
    }

    private RuleMatchResult evaluateTextContainsRule(RiskRule rule, String actualValue) {
        if (!StringUtils.hasText(actualValue) || !StringUtils.hasText(rule.getRuleContent())) {
            return RuleMatchResult.notMatched();
        }
        String actual = actualValue.toUpperCase(Locale.ROOT);
        String[] keywords = rule.getRuleContent().split(",");
        for (String keyword : keywords) {
            String normalized = keyword.trim().toUpperCase(Locale.ROOT);
            if (StringUtils.hasText(normalized) && actual.contains(normalized)) {
                return RuleMatchResult.matched(actualValue);
            }
        }
        return RuleMatchResult.notMatched();
    }

    private RuleMatchResult evaluateTimeRule(RiskRule rule, RiskTransaction transaction) {
        LocalDateTime transactionTime = transaction.getTransactionTime();
        if (transactionTime == null) {
            return RuleMatchResult.notMatched();
        }
        int hour = transactionTime.getHour();
        boolean matched = hour >= 23 || hour < 6;
        return matched ? RuleMatchResult.matched(String.valueOf(hour)) : RuleMatchResult.notMatched();
    }

    private RuleMatchResult evaluateBlacklistRule(RiskRule rule, RiskTransaction transaction) {
        if (!StringUtils.hasText(rule.getRuleContent())) {
            return RuleMatchResult.notMatched();
        }
        String customerId = toText(transaction.getCustomerId());
        String accountNo = toText(transaction.getAccountNo());
        String[] values = rule.getRuleContent().split(",");
        for (String value : values) {
            String item = value.trim();
            if (!StringUtils.hasText(item)) {
                continue;
            }
            if (item.equalsIgnoreCase(customerId) || item.equalsIgnoreCase(accountNo)) {
                return RuleMatchResult.matched(item);
            }
        }
        return RuleMatchResult.notMatched();
    }

    private boolean compare(BigDecimal actual, BigDecimal threshold, String operator) {
        String compareOperator = StringUtils.hasText(operator) ? operator.toUpperCase(Locale.ROOT) : "GTE";
        int result = actual.compareTo(threshold);
        return switch (compareOperator) {
            case "GT" -> result > 0;
            case "GTE" -> result >= 0;
            case "LT" -> result < 0;
            case "LTE" -> result <= 0;
            case "EQ" -> result == 0;
            default -> result >= 0;
        };
    }

    private boolean existsHit(Long transactionId, Long ruleId) {
        Long count = riskRuleHitMapper.selectCount(new LambdaQueryWrapper<RiskRuleHit>()
                .eq(RiskRuleHit::getTransactionId, transactionId)
                .eq(RiskRuleHit::getRuleId, ruleId));
        return count != null && count > 0;
    }

    private RiskRuleHit buildRuleHit(RiskTransaction transaction, RiskRule rule, String hitValue) {
        LocalDateTime now = LocalDateTime.now();
        RiskRuleHit hit = new RiskRuleHit();
        hit.setHitCode(generateHitCode());
        hit.setTransactionId(transaction.getId());
        hit.setTransactionNo(transaction.getTransactionNo());
        hit.setAccountNo(transaction.getAccountNo());
        hit.setCustomerId(toText(transaction.getCustomerId()));
        hit.setCustomerName(transaction.getCustomerName());
        hit.setRuleId(rule.getId());
        hit.setRuleCode(rule.getRuleCode());
        hit.setRuleName(rule.getRuleName());
        hit.setRuleType(rule.getRuleType());
        hit.setHitValue(hitValue);
        hit.setThresholdValue(rule.getThresholdValue());
        hit.setRiskLevel(rule.getRiskLevel());
        hit.setRiskScore(rule.getRiskScore());
        hit.setHitTime(now);
        hit.setStatus(0);
        hit.setCreateBy("system");
        hit.setCreateTime(now);
        hit.setDeleted(0);
        hit.setRemark("R3-03 rule engine generated");
        return hit;
    }

    private String generateHitCode() {
        return "HIT" + LocalDateTime.now().format(HIT_CODE_TIME_FORMATTER)
                + ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private RiskRuleHitPageVO toVO(RiskRuleHit hit) {
        RiskRuleHitPageVO vo = new RiskRuleHitPageVO();
        vo.setId(hit.getId());
        vo.setHitCode(hit.getHitCode());
        vo.setTransactionId(hit.getTransactionId());
        vo.setTransactionNo(hit.getTransactionNo());
        vo.setAccountNo(hit.getAccountNo());
        vo.setCustomerId(hit.getCustomerId());
        vo.setCustomerName(hit.getCustomerName());
        vo.setRuleId(hit.getRuleId());
        vo.setRuleCode(hit.getRuleCode());
        vo.setRuleName(hit.getRuleName());
        vo.setRuleType(hit.getRuleType());
        vo.setHitValue(hit.getHitValue());
        vo.setThresholdValue(hit.getThresholdValue());
        vo.setRiskLevel(hit.getRiskLevel());
        vo.setRiskScore(hit.getRiskScore());
        vo.setHitTime(hit.getHitTime());
        vo.setStatus(hit.getStatus());
        vo.setCreateTime(hit.getCreateTime());
        vo.setRemark(hit.getRemark());
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

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit < 1) {
            return 50;
        }
        return Math.min(limit, 200);
    }

    private String toText(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private record RuleMatchResult(boolean matched, String hitValue) {

        private static RuleMatchResult matched(String hitValue) {
            return new RuleMatchResult(true, hitValue);
        }

        private static RuleMatchResult notMatched() {
            return new RuleMatchResult(false, null);
        }
    }
}