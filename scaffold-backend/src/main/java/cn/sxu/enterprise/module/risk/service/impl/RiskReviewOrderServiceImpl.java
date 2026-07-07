package cn.sxu.enterprise.module.risk.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskReviewApproveRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewOrderCreateRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewRejectRequest;
import cn.sxu.enterprise.module.risk.entity.RiskReviewOrder;
import cn.sxu.enterprise.module.risk.entity.RiskRuleHit;
import cn.sxu.enterprise.module.risk.entity.RiskTransaction;
import cn.sxu.enterprise.module.risk.mapper.RiskReviewOrderMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleHitMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskTransactionMapper;
import cn.sxu.enterprise.module.risk.service.RiskReviewOrderService;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskReviewSummaryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RiskReviewOrderServiceImpl implements RiskReviewOrderService {

    private static final DateTimeFormatter REVIEW_CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final int REVIEW_THRESHOLD_SCORE = 60;
    private static final int REJECT_THRESHOLD_SCORE = 90;

    @Resource
    private RiskReviewOrderMapper riskReviewOrderMapper;

    @Resource
    private RiskRuleHitMapper riskRuleHitMapper;

    @Resource
    private RiskTransactionMapper riskTransactionMapper;

    @Override
    public PageResult<RiskReviewOrderPageVO> pageReviewOrders(RiskReviewOrderPageQuery query) {
        Long pageNo = normalizePageNo(query == null ? null : query.getPageNo());
        Long pageSize = normalizePageSize(query == null ? null : query.getPageSize());

        LambdaQueryWrapper<RiskReviewOrder> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            wrapper.like(StringUtils.hasText(query.getReviewOrderCode()), RiskReviewOrder::getReviewOrderCode, query.getReviewOrderCode())
                    .like(StringUtils.hasText(query.getTransactionNo()), RiskReviewOrder::getTransactionNo, query.getTransactionNo())
                    .like(StringUtils.hasText(query.getAccountNo()), RiskReviewOrder::getAccountNo, query.getAccountNo())
                    .like(StringUtils.hasText(query.getCustomerName()), RiskReviewOrder::getCustomerName, query.getCustomerName())
                    .like(StringUtils.hasText(query.getMerchantName()), RiskReviewOrder::getMerchantName, query.getMerchantName())
                    .eq(StringUtils.hasText(query.getTransactionType()), RiskReviewOrder::getTransactionType, query.getTransactionType())
                    .eq(StringUtils.hasText(query.getChannel()), RiskReviewOrder::getChannel, query.getChannel())
                    .eq(query.getRiskLevel() != null, RiskReviewOrder::getRiskLevel, query.getRiskLevel())
                    .eq(StringUtils.hasText(query.getRiskResult()), RiskReviewOrder::getRiskResult, query.getRiskResult())
                    .eq(query.getReviewStatus() != null, RiskReviewOrder::getReviewStatus, query.getReviewStatus())
                    .eq(query.getStatus() != null, RiskReviewOrder::getStatus, query.getStatus())
                    .ge(query.getBeginTime() != null, RiskReviewOrder::getCreateTime, query.getBeginTime())
                    .le(query.getEndTime() != null, RiskReviewOrder::getCreateTime, query.getEndTime());
        }
        wrapper.orderByDesc(RiskReviewOrder::getCreateTime).orderByDesc(RiskReviewOrder::getId);

        Page<RiskReviewOrder> page = riskReviewOrderMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<RiskReviewOrderPageVO> records = page.getRecords().stream().map(this::toVO).toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    @Override
    public RiskReviewSummaryVO summary() {
        List<RiskReviewOrder> orders = riskReviewOrderMapper.selectList(new LambdaQueryWrapper<RiskReviewOrder>());
        LocalDate today = LocalDate.now();
        int scoreSum = 0;

        RiskReviewSummaryVO summary = new RiskReviewSummaryVO();
        summary.setTotalOrderCount((long) orders.size());
        summary.setPendingCount(0L);
        summary.setApprovedCount(0L);
        summary.setRejectedCount(0L);
        summary.setLowRiskCount(0L);
        summary.setMediumRiskCount(0L);
        summary.setHighRiskCount(0L);
        summary.setReviewResultReviewCount(0L);
        summary.setReviewResultRejectCount(0L);
        summary.setTodayOrderCount(0L);

        for (RiskReviewOrder order : orders) {
            if (Integer.valueOf(0).equals(order.getReviewStatus())) {
                summary.setPendingCount(summary.getPendingCount() + 1);
            } else if (Integer.valueOf(1).equals(order.getReviewStatus())) {
                summary.setApprovedCount(summary.getApprovedCount() + 1);
            } else if (Integer.valueOf(2).equals(order.getReviewStatus())) {
                summary.setRejectedCount(summary.getRejectedCount() + 1);
            }

            if (Integer.valueOf(1).equals(order.getRiskLevel())) {
                summary.setLowRiskCount(summary.getLowRiskCount() + 1);
            } else if (Integer.valueOf(2).equals(order.getRiskLevel())) {
                summary.setMediumRiskCount(summary.getMediumRiskCount() + 1);
            } else if (Integer.valueOf(3).equals(order.getRiskLevel())) {
                summary.setHighRiskCount(summary.getHighRiskCount() + 1);
            }

            if ("REVIEW".equals(order.getRiskResult())) {
                summary.setReviewResultReviewCount(summary.getReviewResultReviewCount() + 1);
            } else if ("REJECT".equals(order.getRiskResult())) {
                summary.setReviewResultRejectCount(summary.getReviewResultRejectCount() + 1);
            }

            if (order.getCreateTime() != null && today.equals(order.getCreateTime().toLocalDate())) {
                summary.setTodayOrderCount(summary.getTodayOrderCount() + 1);
            }

            scoreSum += order.getTotalScore() == null ? 0 : order.getTotalScore();
        }

        BigDecimal averageScore = orders.isEmpty()
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(scoreSum).divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
        summary.setAverageScore(averageScore);
        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RiskReviewOrderPageVO> createFromTransaction(RiskReviewOrderCreateRequest request) {
        List<RiskTransaction> transactions = loadTransactions(request);
        List<RiskReviewOrderPageVO> result = new ArrayList<>();

        for (RiskTransaction transaction : transactions) {
            if (transaction == null || transaction.getId() == null || existsReviewOrder(transaction.getId())) {
                continue;
            }

            List<RiskRuleHit> hits = riskRuleHitMapper.selectList(new LambdaQueryWrapper<RiskRuleHit>()
                    .eq(RiskRuleHit::getTransactionId, transaction.getId())
                    .eq(RiskRuleHit::getStatus, 0)
                    .orderByDesc(RiskRuleHit::getRiskScore)
                    .orderByDesc(RiskRuleHit::getHitTime));
            int totalScore = hits.stream()
                    .map(RiskRuleHit::getRiskScore)
                    .filter(score -> score != null && score > 0)
                    .mapToInt(Integer::intValue)
                    .sum();

            if (totalScore < REVIEW_THRESHOLD_SCORE) {
                continue;
            }

            RiskReviewOrder order = buildReviewOrder(transaction, totalScore);
            riskReviewOrderMapper.insert(order);
            result.add(toVO(order));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskReviewOrderPageVO approve(Long id, RiskReviewApproveRequest request) {
        RiskReviewOrder order = getExistingOrder(id);
        ensurePending(order);

        LocalDateTime now = LocalDateTime.now();
        order.setReviewStatus(1);
        order.setRiskResult("PASS");
        order.setReviewerUserId(request == null ? null : request.getReviewerUserId());
        order.setReviewerUsername(normalizeReviewerUsername(request == null ? null : request.getReviewerUsername()));
        order.setReviewTime(now);
        order.setReviewResult(request == null ? "人工审核通过" : request.getReviewResult());
        order.setUpdateBy(order.getReviewerUsername());
        order.setUpdateTime(now);
        riskReviewOrderMapper.updateById(order);
        return toVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskReviewOrderPageVO reject(Long id, RiskReviewRejectRequest request) {
        RiskReviewOrder order = getExistingOrder(id);
        ensurePending(order);

        LocalDateTime now = LocalDateTime.now();
        order.setReviewStatus(2);
        order.setRiskResult("REJECT");
        order.setReviewerUserId(request == null ? null : request.getReviewerUserId());
        order.setReviewerUsername(normalizeReviewerUsername(request == null ? null : request.getReviewerUsername()));
        order.setReviewTime(now);
        order.setReviewResult(request == null ? "人工审核拒绝" : request.getReviewResult());
        order.setStatus(1);
        order.setUpdateBy(order.getReviewerUsername());
        order.setUpdateTime(now);
        riskReviewOrderMapper.updateById(order);
        return toVO(order);
    }

    private List<RiskTransaction> loadTransactions(RiskReviewOrderCreateRequest request) {
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

    private boolean existsReviewOrder(Long transactionId) {
        Long count = riskReviewOrderMapper.selectCount(new LambdaQueryWrapper<RiskReviewOrder>()
                .eq(RiskReviewOrder::getTransactionId, transactionId));
        return count != null && count > 0;
    }

    private RiskReviewOrder buildReviewOrder(RiskTransaction transaction, int totalScore) {
        LocalDateTime now = LocalDateTime.now();
        RiskReviewOrder order = new RiskReviewOrder();
        order.setReviewOrderCode(generateReviewOrderCode());
        order.setTransactionId(transaction.getId());
        order.setTransactionNo(transaction.getTransactionNo());
        order.setAccountNo(transaction.getAccountNo());
        order.setCustomerId(toText(transaction.getCustomerId()));
        order.setCustomerName(transaction.getCustomerName());
        order.setMerchantId(toText(transaction.getMerchantId()));
        order.setMerchantName(transaction.getMerchantName());
        order.setTransactionType(transaction.getTransactionType());
        order.setChannel(transaction.getChannel());
        order.setAmount(transaction.getAmount());
        order.setCurrency(transaction.getCurrency());
        order.setTotalScore(totalScore);
        order.setRiskLevel(resolveRiskLevel(totalScore));
        order.setRiskResult(resolveRiskResult(totalScore));
        order.setReviewStatus(0);
        order.setStatus(0);
        order.setCreateBy("system");
        order.setCreateTime(now);
        order.setDeleted(0);
        order.setRemark("R3-04 risk score generated from risk_rule_hit");
        return order;
    }

    private RiskReviewOrder getExistingOrder(Long id) {
        if (id == null) {
            throw new BusinessException(500, "审核单ID不能为空");
        }
        RiskReviewOrder order = riskReviewOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(500, "审核单不存在");
        }
        return order;
    }

    private void ensurePending(RiskReviewOrder order) {
        if (!Integer.valueOf(0).equals(order.getReviewStatus())) {
            throw new BusinessException(500, "审核单已处理，不能重复审核");
        }
    }

    private String generateReviewOrderCode() {
        return "REV" + LocalDateTime.now().format(REVIEW_CODE_TIME_FORMATTER)
                + ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private Integer resolveRiskLevel(int totalScore) {
        if (totalScore >= 80) {
            return 3;
        }
        if (totalScore >= 40) {
            return 2;
        }
        return 1;
    }

    private String resolveRiskResult(int totalScore) {
        return totalScore >= REJECT_THRESHOLD_SCORE ? "REJECT" : "REVIEW";
    }

    private RiskReviewOrderPageVO toVO(RiskReviewOrder order) {
        RiskReviewOrderPageVO vo = new RiskReviewOrderPageVO();
        vo.setId(order.getId());
        vo.setReviewOrderCode(order.getReviewOrderCode());
        vo.setTransactionId(order.getTransactionId());
        vo.setTransactionNo(order.getTransactionNo());
        vo.setAccountNo(order.getAccountNo());
        vo.setCustomerId(order.getCustomerId());
        vo.setCustomerName(order.getCustomerName());
        vo.setMerchantId(order.getMerchantId());
        vo.setMerchantName(order.getMerchantName());
        vo.setTransactionType(order.getTransactionType());
        vo.setChannel(order.getChannel());
        vo.setAmount(order.getAmount());
        vo.setCurrency(order.getCurrency());
        vo.setTotalScore(order.getTotalScore());
        vo.setRiskLevel(order.getRiskLevel());
        vo.setRiskResult(order.getRiskResult());
        vo.setReviewStatus(order.getReviewStatus());
        vo.setReviewerUserId(order.getReviewerUserId());
        vo.setReviewerUsername(order.getReviewerUsername());
        vo.setReviewTime(order.getReviewTime());
        vo.setReviewResult(order.getReviewResult());
        vo.setStatus(order.getStatus());
        vo.setCreateTime(order.getCreateTime());
        vo.setRemark(order.getRemark());
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

    private String normalizeReviewerUsername(String username) {
        return StringUtils.hasText(username) ? username : "admin";
    }

    private String toText(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
