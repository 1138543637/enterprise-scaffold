package cn.sxu.enterprise.module.risk.vo;

import java.math.BigDecimal;

public class RiskReviewSummaryVO {

    private Long totalOrderCount;

    private Long pendingCount;

    private Long approvedCount;

    private Long rejectedCount;

    private Long lowRiskCount;

    private Long mediumRiskCount;

    private Long highRiskCount;

    private Long reviewResultReviewCount;

    private Long reviewResultRejectCount;

    private Long todayOrderCount;

    private BigDecimal averageScore;

    public Long getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Long totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(Long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public Long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(Long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public Long getLowRiskCount() {
        return lowRiskCount;
    }

    public void setLowRiskCount(Long lowRiskCount) {
        this.lowRiskCount = lowRiskCount;
    }

    public Long getMediumRiskCount() {
        return mediumRiskCount;
    }

    public void setMediumRiskCount(Long mediumRiskCount) {
        this.mediumRiskCount = mediumRiskCount;
    }

    public Long getHighRiskCount() {
        return highRiskCount;
    }

    public void setHighRiskCount(Long highRiskCount) {
        this.highRiskCount = highRiskCount;
    }

    public Long getReviewResultReviewCount() {
        return reviewResultReviewCount;
    }

    public void setReviewResultReviewCount(Long reviewResultReviewCount) {
        this.reviewResultReviewCount = reviewResultReviewCount;
    }

    public Long getReviewResultRejectCount() {
        return reviewResultRejectCount;
    }

    public void setReviewResultRejectCount(Long reviewResultRejectCount) {
        this.reviewResultRejectCount = reviewResultRejectCount;
    }

    public Long getTodayOrderCount() {
        return todayOrderCount;
    }

    public void setTodayOrderCount(Long todayOrderCount) {
        this.todayOrderCount = todayOrderCount;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }
}
