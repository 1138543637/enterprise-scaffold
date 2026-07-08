package cn.sxu.enterprise.module.risk.vo;

import cn.sxu.enterprise.module.risk.entity.RiskTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiskRecentTransactionVO {

    private Long id;
    private String transactionNo;
    private String accountNo;
    private String customerName;
    private String merchantName;
    private String transactionType;
    private String transactionTypeName;
    private String channel;
    private String channelName;
    private BigDecimal amount;
    private String currency;
    private String location;
    private Integer riskFlag;
    private String riskFlagName;
    private LocalDateTime transactionTime;

    public static RiskRecentTransactionVO fromEntity(RiskTransaction entity) {
        RiskRecentTransactionVO vo = new RiskRecentTransactionVO();
        vo.setId(entity.getId());
        vo.setTransactionNo(entity.getTransactionNo());
        vo.setAccountNo(entity.getAccountNo());
        vo.setCustomerName(entity.getCustomerName());
        vo.setMerchantName(entity.getMerchantName());
        vo.setTransactionType(entity.getTransactionType());
        vo.setTransactionTypeName(transactionTypeName(entity.getTransactionType()));
        vo.setChannel(entity.getChannel());
        vo.setChannelName(channelName(entity.getChannel()));
        vo.setAmount(entity.getAmount());
        vo.setCurrency(entity.getCurrency());
        vo.setLocation(entity.getLocation());
        vo.setRiskFlag(entity.getRiskFlag());
        vo.setRiskFlagName(entity.getRiskFlag() != null && entity.getRiskFlag() == 1 ? "风险交易" : "正常交易");
        vo.setTransactionTime(entity.getTransactionTime());
        return vo;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(Integer riskFlag) {
        this.riskFlag = riskFlag;
    }

    public String getRiskFlagName() {
        return riskFlagName;
    }

    public void setRiskFlagName(String riskFlagName) {
        this.riskFlagName = riskFlagName;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
}
