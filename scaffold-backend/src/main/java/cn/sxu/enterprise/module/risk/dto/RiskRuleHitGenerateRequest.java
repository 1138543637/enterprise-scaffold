package cn.sxu.enterprise.module.risk.dto;

public class RiskRuleHitGenerateRequest {

    private Long transactionId;

    private Integer limit;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
