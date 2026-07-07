package cn.sxu.enterprise.module.risk.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class RiskTransactionSimulateRequest {

    @Min(value = 1, message = "模拟条数不能小于1")
    @Max(value = 100, message = "模拟条数不能大于100")
    private Integer count = 10;

    private String accountNo;

    private String customerName;

    private String transactionType;

    private String channel;

    @DecimalMin(value = "0.01", message = "最小金额必须大于0")
    private BigDecimal minAmount;

    @DecimalMin(value = "0.01", message = "最大金额必须大于0")
    private BigDecimal maxAmount;

    private Integer riskFlag;

    private String remark;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(Integer riskFlag) {
        this.riskFlag = riskFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
