package cn.sxu.enterprise.module.risk.vo;

public class RiskDashboardSummaryVO {

    private Long transactionTotal;
    private Long riskTransactionTotal;
    private Long ruleTotal;
    private Long ruleHitTotal;
    private Long pendingReviewTotal;
    private Long approvedReviewTotal;
    private Long rejectedReviewTotal;

    public Long getTransactionTotal() {
        return transactionTotal;
    }

    public void setTransactionTotal(Long transactionTotal) {
        this.transactionTotal = transactionTotal;
    }

    public Long getRiskTransactionTotal() {
        return riskTransactionTotal;
    }

    public void setRiskTransactionTotal(Long riskTransactionTotal) {
        this.riskTransactionTotal = riskTransactionTotal;
    }

    public Long getRuleTotal() {
        return ruleTotal;
    }

    public void setRuleTotal(Long ruleTotal) {
        this.ruleTotal = ruleTotal;
    }

    public Long getRuleHitTotal() {
        return ruleHitTotal;
    }

    public void setRuleHitTotal(Long ruleHitTotal) {
        this.ruleHitTotal = ruleHitTotal;
    }

    public Long getPendingReviewTotal() {
        return pendingReviewTotal;
    }

    public void setPendingReviewTotal(Long pendingReviewTotal) {
        this.pendingReviewTotal = pendingReviewTotal;
    }

    public Long getApprovedReviewTotal() {
        return approvedReviewTotal;
    }

    public void setApprovedReviewTotal(Long approvedReviewTotal) {
        this.approvedReviewTotal = approvedReviewTotal;
    }

    public Long getRejectedReviewTotal() {
        return rejectedReviewTotal;
    }

    public void setRejectedReviewTotal(Long rejectedReviewTotal) {
        this.rejectedReviewTotal = rejectedReviewTotal;
    }
}
