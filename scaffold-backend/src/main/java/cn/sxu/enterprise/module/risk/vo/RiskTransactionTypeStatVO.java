package cn.sxu.enterprise.module.risk.vo;

public class RiskTransactionTypeStatVO {

    private String transactionType;
    private String transactionTypeName;
    private Long total;
    private Long riskTotal;

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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getRiskTotal() {
        return riskTotal;
    }

    public void setRiskTotal(Long riskTotal) {
        this.riskTotal = riskTotal;
    }
}
