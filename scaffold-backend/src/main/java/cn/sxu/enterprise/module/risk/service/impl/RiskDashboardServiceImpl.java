package cn.sxu.enterprise.module.risk.service.impl;

import cn.sxu.enterprise.module.risk.entity.RiskReviewOrder;
import cn.sxu.enterprise.module.risk.entity.RiskRule;
import cn.sxu.enterprise.module.risk.entity.RiskRuleHit;
import cn.sxu.enterprise.module.risk.entity.RiskTransaction;
import cn.sxu.enterprise.module.risk.mapper.RiskReviewOrderMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleHitMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskRuleMapper;
import cn.sxu.enterprise.module.risk.mapper.RiskTransactionMapper;
import cn.sxu.enterprise.module.risk.service.RiskDashboardService;
import cn.sxu.enterprise.module.risk.vo.RiskChannelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskDashboardSummaryVO;
import cn.sxu.enterprise.module.risk.vo.RiskLevelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentReviewOrderVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentRuleHitVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentTransactionVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionTypeStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RiskDashboardServiceImpl implements RiskDashboardService {

    private final RiskTransactionMapper riskTransactionMapper;
    private final RiskRuleMapper riskRuleMapper;
    private final RiskRuleHitMapper riskRuleHitMapper;
    private final RiskReviewOrderMapper riskReviewOrderMapper;

    public RiskDashboardServiceImpl(RiskTransactionMapper riskTransactionMapper,
                                    RiskRuleMapper riskRuleMapper,
                                    RiskRuleHitMapper riskRuleHitMapper,
                                    RiskReviewOrderMapper riskReviewOrderMapper) {
        this.riskTransactionMapper = riskTransactionMapper;
        this.riskRuleMapper = riskRuleMapper;
        this.riskRuleHitMapper = riskRuleHitMapper;
        this.riskReviewOrderMapper = riskReviewOrderMapper;
    }

    @Override
    public RiskDashboardSummaryVO getSummary() {
        RiskDashboardSummaryVO vo = new RiskDashboardSummaryVO();

        Long transactionTotal = riskTransactionMapper.selectCount(new LambdaQueryWrapper<RiskTransaction>());
        Long riskTransactionTotal = riskTransactionMapper.selectCount(
                new LambdaQueryWrapper<RiskTransaction>().eq(RiskTransaction::getRiskFlag, 1)
        );
        Long ruleTotal = riskRuleMapper.selectCount(new LambdaQueryWrapper<RiskRule>());
        Long ruleHitTotal = riskRuleHitMapper.selectCount(new LambdaQueryWrapper<RiskRuleHit>());
        Long pendingReviewTotal = riskReviewOrderMapper.selectCount(
                new LambdaQueryWrapper<RiskReviewOrder>().eq(RiskReviewOrder::getReviewStatus, 0)
        );
        Long approvedReviewTotal = riskReviewOrderMapper.selectCount(
                new LambdaQueryWrapper<RiskReviewOrder>().eq(RiskReviewOrder::getReviewStatus, 1)
        );
        Long rejectedReviewTotal = riskReviewOrderMapper.selectCount(
                new LambdaQueryWrapper<RiskReviewOrder>().eq(RiskReviewOrder::getReviewStatus, 2)
        );

        vo.setTransactionTotal(transactionTotal);
        vo.setRiskTransactionTotal(riskTransactionTotal);
        vo.setRuleTotal(ruleTotal);
        vo.setRuleHitTotal(ruleHitTotal);
        vo.setPendingReviewTotal(pendingReviewTotal);
        vo.setApprovedReviewTotal(approvedReviewTotal);
        vo.setRejectedReviewTotal(rejectedReviewTotal);
        return vo;
    }

    @Override
    public List<RiskChannelStatVO> getChannelStats() {
        QueryWrapper<RiskTransaction> wrapper = new QueryWrapper<>();
        wrapper.select(
                "channel AS channel",
                "COUNT(*) AS total",
                "SUM(CASE WHEN risk_flag = 1 THEN 1 ELSE 0 END) AS risk_total"
        );
        wrapper.groupBy("channel");
        wrapper.orderByDesc("total");

        List<Map<String, Object>> rows = riskTransactionMapper.selectMaps(wrapper);
        List<RiskChannelStatVO> list = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            String channel = toStringValue(row, "channel", "CHANNEL");
            RiskChannelStatVO vo = new RiskChannelStatVO();
            vo.setChannel(channel);
            vo.setChannelName(channelName(channel));
            vo.setTotal(toLong(row, "total", "TOTAL"));
            vo.setRiskTotal(toLong(row, "risk_total", "RISK_TOTAL"));
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<RiskTransactionTypeStatVO> getTransactionTypeStats() {
        QueryWrapper<RiskTransaction> wrapper = new QueryWrapper<>();
        wrapper.select(
                "transaction_type AS transaction_type",
                "COUNT(*) AS total",
                "SUM(CASE WHEN risk_flag = 1 THEN 1 ELSE 0 END) AS risk_total"
        );
        wrapper.groupBy("transaction_type");
        wrapper.orderByDesc("total");

        List<Map<String, Object>> rows = riskTransactionMapper.selectMaps(wrapper);
        List<RiskTransactionTypeStatVO> list = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            String transactionType = toStringValue(row, "transaction_type", "TRANSACTION_TYPE");
            RiskTransactionTypeStatVO vo = new RiskTransactionTypeStatVO();
            vo.setTransactionType(transactionType);
            vo.setTransactionTypeName(transactionTypeName(transactionType));
            vo.setTotal(toLong(row, "total", "TOTAL"));
            vo.setRiskTotal(toLong(row, "risk_total", "RISK_TOTAL"));
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<RiskLevelStatVO> getRiskLevelStats() {
        QueryWrapper<RiskReviewOrder> wrapper = new QueryWrapper<>();
        wrapper.select(
                "risk_level AS risk_level",
                "COUNT(*) AS total"
        );
        wrapper.groupBy("risk_level");
        wrapper.orderByAsc("risk_level");

        List<Map<String, Object>> rows = riskReviewOrderMapper.selectMaps(wrapper);
        List<RiskLevelStatVO> list = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Integer riskLevel = toInteger(row, "risk_level", "RISK_LEVEL");
            RiskLevelStatVO vo = new RiskLevelStatVO();
            vo.setRiskLevel(riskLevel);
            vo.setRiskLevelName(riskLevelName(riskLevel));
            vo.setTotal(toLong(row, "total", "TOTAL"));
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<RiskRecentTransactionVO> getRecentTransactions() {
        List<RiskTransaction> records = riskTransactionMapper.selectList(
                new LambdaQueryWrapper<RiskTransaction>()
                        .orderByDesc(RiskTransaction::getTransactionTime)
                        .orderByDesc(RiskTransaction::getId)
                        .last("LIMIT 10")
        );

        List<RiskRecentTransactionVO> list = new ArrayList<>();
        for (RiskTransaction record : records) {
            list.add(RiskRecentTransactionVO.fromEntity(record));
        }
        return list;
    }

    @Override
    public List<RiskRecentRuleHitVO> getRecentRuleHits() {
        List<RiskRuleHit> records = riskRuleHitMapper.selectList(
                new LambdaQueryWrapper<RiskRuleHit>()
                        .orderByDesc(RiskRuleHit::getHitTime)
                        .orderByDesc(RiskRuleHit::getId)
                        .last("LIMIT 10")
        );

        List<RiskRecentRuleHitVO> list = new ArrayList<>();
        for (RiskRuleHit record : records) {
            list.add(RiskRecentRuleHitVO.fromEntity(record));
        }
        return list;
    }

    @Override
    public List<RiskRecentReviewOrderVO> getRecentReviewOrders() {
        List<RiskReviewOrder> records = riskReviewOrderMapper.selectList(
                new LambdaQueryWrapper<RiskReviewOrder>()
                        .orderByDesc(RiskReviewOrder::getCreateTime)
                        .orderByDesc(RiskReviewOrder::getId)
                        .last("LIMIT 10")
        );

        List<RiskRecentReviewOrderVO> list = new ArrayList<>();
        for (RiskReviewOrder record : records) {
            list.add(RiskRecentReviewOrderVO.fromEntity(record));
        }
        return list;
    }

    private static String channelName(String channel) {
        if ("APP".equals(channel)) {
            return "手机银行";
        }
        if ("WEB".equals(channel)) {
            return "网上银行";
        }
        if ("ATM".equals(channel)) {
            return "ATM";
        }
        if ("POS".equals(channel)) {
            return "POS";
        }
        return channel;
    }

    private static String transactionTypeName(String type) {
        if ("PAYMENT".equals(type)) {
            return "支付";
        }
        if ("TRANSFER".equals(type)) {
            return "转账";
        }
        if ("WITHDRAW".equals(type)) {
            return "取现";
        }
        if ("CONSUME".equals(type)) {
            return "消费";
        }
        return type;
    }

    private static String riskLevelName(Integer riskLevel) {
        if (riskLevel == null) {
            return "未知";
        }
        if (riskLevel == 1) {
            return "低风险";
        }
        if (riskLevel == 2) {
            return "中风险";
        }
        if (riskLevel == 3) {
            return "高风险";
        }
        return "未知";
    }

    private static Object getValue(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            if (row.containsKey(key)) {
                return row.get(key);
            }
        }
        return null;
    }

    private static String toStringValue(Map<String, Object> row, String... keys) {
        Object value = getValue(row, keys);
        return value == null ? "" : String.valueOf(value);
    }

    private static Long toLong(Map<String, Object> row, String... keys) {
        Object value = getValue(row, keys);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private static Integer toInteger(Map<String, Object> row, String... keys) {
        Object value = getValue(row, keys);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
