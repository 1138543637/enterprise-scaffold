package cn.sxu.enterprise.module.risk.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskTransactionSimulateRequest;
import cn.sxu.enterprise.module.risk.entity.RiskTransaction;
import cn.sxu.enterprise.module.risk.mapper.RiskTransactionMapper;
import cn.sxu.enterprise.module.risk.service.RiskTransactionService;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RiskTransactionServiceImpl implements RiskTransactionService {

    private static final BigDecimal DEFAULT_MIN_AMOUNT = new BigDecimal("10.00");
    private static final BigDecimal DEFAULT_MAX_AMOUNT = new BigDecimal("50000.00");
    private static final BigDecimal AUTO_RISK_AMOUNT = new BigDecimal("20000.00");

    private static final String[] TRANSACTION_TYPES = {"PAYMENT", "TRANSFER", "WITHDRAW", "CONSUME"};
    private static final String[] CHANNELS = {"APP", "WEB", "ATM", "POS"};
    private static final String[] CUSTOMER_NAMES = {"张明", "李娜", "王强", "赵敏", "陈晨", "刘洋", "孙磊", "周颖"};
    private static final String[] MERCHANT_NAMES = {"山西能源缴费平台", "太原生活超市", "晋中数码商城", "阳泉交通出行", "大同餐饮连锁", "运城便民服务"};
    private static final String[] LOCATIONS = {"山西太原", "山西大同", "山西阳泉", "山西长治", "山西晋中", "山西运城"};

    private final RiskTransactionMapper riskTransactionMapper;

    public RiskTransactionServiceImpl(RiskTransactionMapper riskTransactionMapper) {
        this.riskTransactionMapper = riskTransactionMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RiskTransactionVO> simulateTransactions(RiskTransactionSimulateRequest request) {
        int count = normalizeCount(request.getCount());
        BigDecimal minAmount = normalizeAmount(request.getMinAmount(), DEFAULT_MIN_AMOUNT);
        BigDecimal maxAmount = normalizeAmount(request.getMaxAmount(), DEFAULT_MAX_AMOUNT);
        if (maxAmount.compareTo(minAmount) < 0) {
            throw new BusinessException(500, "最大金额不能小于最小金额");
        }
        validateRiskFlag(request.getRiskFlag());

        List<RiskTransactionVO> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            RiskTransaction transaction = buildTransaction(request, minAmount, maxAmount, i);
            riskTransactionMapper.insert(transaction);
            result.add(RiskTransactionVO.fromEntity(transaction));
        }
        return result;
    }

    @Override
    public List<RiskTransactionVO> latestTransactions() {
        Page<RiskTransaction> page = riskTransactionMapper.selectPage(
                new Page<>(1, 10),
                new LambdaQueryWrapper<RiskTransaction>()
                        .orderByDesc(RiskTransaction::getTransactionTime)
                        .orderByDesc(RiskTransaction::getId)
        );
        return page.getRecords().stream()
                .map(RiskTransactionVO::fromEntity)
                .toList();
    }

    @Override
    public PageResult<RiskTransactionPageVO> pageTransactions(RiskTransactionPageQuery query) {
        Long pageNo = normalizePageNo(query.getPageNo());
        Long pageSize = normalizePageSize(query.getPageSize());

        Page<RiskTransaction> page = riskTransactionMapper.selectPage(
                new Page<>(pageNo, pageSize),
                buildTransactionWrapper(query)
        );
        List<RiskTransactionPageVO> records = page.getRecords().stream()
                .map(RiskTransactionPageVO::fromEntity)
                .toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    private RiskTransaction buildTransaction(RiskTransactionSimulateRequest request,
                                             BigDecimal minAmount,
                                             BigDecimal maxAmount,
                                             int sequence) {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = randomAmount(minAmount, maxAmount);
        String transactionType = resolveTransactionType(request.getTransactionType());
        String channel = resolveChannel(request.getChannel());
        int riskFlag = resolveRiskFlag(request.getRiskFlag(), amount, channel);
        int transactionStatus = randomTransactionStatus();

        RiskTransaction transaction = new RiskTransaction();
        transaction.setTransactionNo(generateTransactionNo(now, sequence));
        transaction.setAccountNo(resolveAccountNo(request.getAccountNo()));
        transaction.setCustomerId(randomLong(100000L, 999999L));
        transaction.setCustomerName(resolveText(request.getCustomerName(), randomItem(CUSTOMER_NAMES)));
        transaction.setMerchantId("M" + randomLong(10000L, 99999L));
        transaction.setMerchantName(randomItem(MERCHANT_NAMES));
        transaction.setTransactionType(transactionType);
        transaction.setChannel(channel);
        transaction.setAmount(amount);
        transaction.setCurrency("CNY");
        transaction.setIpAddr(generateIpAddr());
        transaction.setDeviceId("DEV-" + randomLong(100000L, 999999L));
        transaction.setLocation(randomItem(LOCATIONS));
        transaction.setTransactionTime(now.minusSeconds(ThreadLocalRandom.current().nextInt(0, 3600)));
        transaction.setTransactionStatus(transactionStatus);
        transaction.setRiskFlag(riskFlag);
        transaction.setStatus(riskFlag == 1 ? 1 : 0);
        transaction.setCreateBy("system");
        transaction.setCreateTime(now);
        transaction.setUpdateBy("system");
        transaction.setUpdateTime(now);
        transaction.setDeleted(0);
        transaction.setRemark(resolveText(request.getRemark(), "R3-02交易模拟生成"));
        return transaction;
    }

    private LambdaQueryWrapper<RiskTransaction> buildTransactionWrapper(RiskTransactionPageQuery query) {
        LambdaQueryWrapper<RiskTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getTransactionNo()), RiskTransaction::getTransactionNo, query.getTransactionNo())
                .like(StringUtils.hasText(query.getAccountNo()), RiskTransaction::getAccountNo, query.getAccountNo())
                .like(StringUtils.hasText(query.getCustomerName()), RiskTransaction::getCustomerName, query.getCustomerName())
                .like(StringUtils.hasText(query.getMerchantName()), RiskTransaction::getMerchantName, query.getMerchantName())
                .eq(StringUtils.hasText(query.getTransactionType()), RiskTransaction::getTransactionType, query.getTransactionType())
                .eq(StringUtils.hasText(query.getChannel()), RiskTransaction::getChannel, query.getChannel())
                .eq(query.getTransactionStatus() != null, RiskTransaction::getTransactionStatus, query.getTransactionStatus())
                .eq(query.getRiskFlag() != null, RiskTransaction::getRiskFlag, query.getRiskFlag())
                .eq(query.getStatus() != null, RiskTransaction::getStatus, query.getStatus())
                .ge(query.getBeginTime() != null, RiskTransaction::getTransactionTime, query.getBeginTime())
                .le(query.getEndTime() != null, RiskTransaction::getTransactionTime, query.getEndTime())
                .orderByDesc(RiskTransaction::getTransactionTime)
                .orderByDesc(RiskTransaction::getId);
        return wrapper;
    }

    private int normalizeCount(Integer count) {
        if (count == null) {
            return 10;
        }
        if (count < 1 || count > 100) {
            throw new BusinessException(500, "模拟条数范围必须是1到100");
        }
        return count;
    }

    private Long normalizePageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            return 1L;
        }
        return pageNo;
    }

    private Long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 100L);
    }

    private BigDecimal normalizeAmount(BigDecimal amount, BigDecimal defaultValue) {
        if (amount == null) {
            return defaultValue;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal randomAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        long minCents = minAmount.movePointRight(2).longValue();
        long maxCents = maxAmount.movePointRight(2).longValue();
        long cents = minCents == maxCents
                ? minCents
                : ThreadLocalRandom.current().nextLong(minCents, maxCents + 1);
        return BigDecimal.valueOf(cents, 2);
    }

    private String resolveTransactionType(String transactionType) {
        if (!StringUtils.hasText(transactionType)) {
            return randomItem(TRANSACTION_TYPES);
        }
        return switch (transactionType) {
            case "PAYMENT", "TRANSFER", "WITHDRAW", "CONSUME" -> transactionType;
            default -> throw new BusinessException(500, "交易类型只能是 PAYMENT、TRANSFER、WITHDRAW、CONSUME");
        };
    }

    private String resolveChannel(String channel) {
        if (!StringUtils.hasText(channel)) {
            return randomItem(CHANNELS);
        }
        return switch (channel) {
            case "APP", "WEB", "ATM", "POS" -> channel;
            default -> throw new BusinessException(500, "交易渠道只能是 APP、WEB、ATM、POS");
        };
    }

    private void validateRiskFlag(Integer riskFlag) {
        if (riskFlag != null && riskFlag != 0 && riskFlag != 1) {
            throw new BusinessException(500, "风险标记只能是0或1");
        }
    }

    private int resolveRiskFlag(Integer requestRiskFlag, BigDecimal amount, String channel) {
        if (requestRiskFlag != null) {
            return requestRiskFlag;
        }
        boolean highAmount = amount.compareTo(AUTO_RISK_AMOUNT) >= 0;
        boolean atmLargeWithdraw = "ATM".equals(channel) && amount.compareTo(new BigDecimal("5000.00")) >= 0;
        boolean randomHit = ThreadLocalRandom.current().nextInt(100) < 10;
        return highAmount || atmLargeWithdraw || randomHit ? 1 : 0;
    }

    private int randomTransactionStatus() {
        int random = ThreadLocalRandom.current().nextInt(100);
        if (random < 85) {
            return 0;
        }
        if (random < 95) {
            return 2;
        }
        return 1;
    }

    private String resolveAccountNo(String accountNo) {
        if (StringUtils.hasText(accountNo)) {
            return accountNo;
        }
        return "622202" + randomLong(1000000000L, 9999999999L);
    }

    private String resolveText(String input, String defaultValue) {
        return StringUtils.hasText(input) ? input : defaultValue;
    }

    private String generateTransactionNo(LocalDateTime now, int sequence) {
        String timePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "RT" + timePart + String.format("%03d", sequence) + random;
    }

    private String generateIpAddr() {
        return "10."
                + ThreadLocalRandom.current().nextInt(1, 255)
                + "."
                + ThreadLocalRandom.current().nextInt(1, 255)
                + "."
                + ThreadLocalRandom.current().nextInt(1, 255);
    }

    private long randomLong(long minInclusive, long maxInclusive) {
        return ThreadLocalRandom.current().nextLong(minInclusive, maxInclusive + 1);
    }

    private String randomItem(String[] values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
