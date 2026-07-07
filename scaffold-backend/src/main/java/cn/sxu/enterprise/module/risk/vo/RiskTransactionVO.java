package cn.sxu.enterprise.module.risk.vo;

import cn.sxu.enterprise.module.risk.entity.RiskTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiskTransactionVO {

    private Long id;

    private String transactionNo;

    private String accountNo;

    private Long customerId;

    private String customerName;

    private String merchantId;

    private String merchantName;

    private String transactionType;

    private String transactionTypeName;

    private String channel;

    private String channelName;

    private BigDecimal amount;

    private String currency;

    private String ipAddr;

    private String deviceId;

    private String location;

    private LocalDateTime transactionTime;

    private Integer transactionStatus;

    private String transactionStatusName;

    private Integer riskFlag;

    private String riskFlagName;

    private Integer status;

    private String statusName;

    private LocalDateTime createTime;

    private String remark;

    public static RiskTransactionVO fromEntity(RiskTransaction entity) {
        RiskTransactionVO vo = new RiskTransactionVO();
        fillFromEntity(vo, entity);
        return vo;
    }

    protected static void fillFromEntity(RiskTransactionVO vo, RiskTransaction entity) {
        vo.setId(entity.getId());
        vo.setTransactionNo(entity.getTransactionNo());
        vo.setAccountNo(entity.getAccountNo());
        vo.setCustomerId(entity.getCustomerId());
        vo.setCustomerName(entity.getCustomerName());
        vo.setMerchantId(entity.getMerchantId());
        vo.setMerchantName(entity.getMerchantName());
        vo.setTransactionType(entity.getTransactionType());
        vo.setTransactionTypeName(toTransactionTypeName(entity.getTransactionType()));
        vo.setChannel(entity.getChannel());
        vo.setChannelName(toChannelName(entity.getChannel()));
        vo.setAmount(entity.getAmount());
        vo.setCurrency(entity.getCurrency());
        vo.setIpAddr(entity.getIpAddr());
        vo.setDeviceId(entity.getDeviceId());
        vo.setLocation(entity.getLocation());
        vo.setTransactionTime(entity.getTransactionTime());
        vo.setTransactionStatus(entity.getTransactionStatus());
        vo.setTransactionStatusName(toTransactionStatusName(entity.getTransactionStatus()));
        vo.setRiskFlag(entity.getRiskFlag());
        vo.setRiskFlagName(toRiskFlagName(entity.getRiskFlag()));
        vo.setStatus(entity.getStatus());
        vo.setStatusName(toStatusName(entity.getStatus()));
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
    }

    private static String toTransactionTypeName(String transactionType) {
        if (transactionType == null) {
            return "未知";
        }
        return switch (transactionType) {
            case "PAYMENT" -> "支付";
            case "TRANSFER" -> "转账";
            case "WITHDRAW" -> "取现";
            case "CONSUME" -> "消费";
            default -> "未知";
        };
    }

    private static String toChannelName(String channel) {
        if (channel == null) {
            return "未知";
        }
        return switch (channel) {
            case "APP" -> "手机银行";
            case "WEB" -> "网上银行";
            case "ATM" -> "ATM";
            case "POS" -> "POS";
            default -> "未知";
        };
    }

    private static String toTransactionStatusName(Integer transactionStatus) {
        if (transactionStatus == null) {
            return "未知";
        }
        return switch (transactionStatus) {
            case 0 -> "成功";
            case 1 -> "失败";
            case 2 -> "处理中";
            default -> "未知";
        };
    }

    private static String toRiskFlagName(Integer riskFlag) {
        if (riskFlag == null) {
            return "未知";
        }
        return switch (riskFlag) {
            case 0 -> "未命中风险";
            case 1 -> "命中风险";
            default -> "未知";
        };
    }

    private static String toStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "正常";
            case 1 -> "异常";
            default -> "未知";
        };
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Integer getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionStatusName() {
        return transactionStatusName;
    }

    public void setTransactionStatusName(String transactionStatusName) {
        this.transactionStatusName = transactionStatusName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
