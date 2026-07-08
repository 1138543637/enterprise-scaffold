package cn.sxu.enterprise.module.risk.vo;

import cn.sxu.enterprise.module.risk.entity.RiskReviewOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiskRecentReviewOrderVO {

    private Long id;
    private String reviewOrderCode;
    private String transactionNo;
    private String customerName;
    private BigDecimal amount;
    private String currency;
    private Integer totalScore;
    private Integer riskLevel;
    private String riskLevelName;
    private String riskResult;
    private String riskResultName;
    private Integer reviewStatus;
    private String reviewStatusName;
    private LocalDateTime createTime;

    public static RiskRecentReviewOrderVO fromEntity(RiskReviewOrder entity) {
        RiskRecentReviewOrderVO vo = new RiskRecentReviewOrderVO();
        vo.setId(entity.getId());
        vo.setReviewOrderCode(entity.getReviewOrderCode());
        vo.setTransactionNo(entity.getTransactionNo());
        vo.setCustomerName(entity.getCustomerName());
        vo.setAmount(entity.getAmount());
        vo.setCurrency(entity.getCurrency());
        vo.setTotalScore(entity.getTotalScore());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setRiskLevelName(riskLevelName(entity.getRiskLevel()));
        vo.setRiskResult(entity.getRiskResult());
        vo.setRiskResultName(riskResultName(entity.getRiskResult()));
        vo.setReviewStatus(entity.getReviewStatus());
        vo.setReviewStatusName(reviewStatusName(entity.getReviewStatus()));
        vo.setCreateTime(entity.getCreateTime());
        return vo;
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

    private static String riskResultName(String riskResult) {
        if ("PASS".equals(riskResult)) {
            return "放行";
        }
        if ("REVIEW".equals(riskResult)) {
            return "人工审核";
        }
        if ("REJECT".equals(riskResult)) {
            return "拒绝";
        }
        return riskResult;
    }

    private static String reviewStatusName(Integer reviewStatus) {
        if (reviewStatus == null) {
            return "未知";
        }
        if (reviewStatus == 0) {
            return "待审核";
        }
        if (reviewStatus == 1) {
            return "审核通过";
        }
        if (reviewStatus == 2) {
            return "审核拒绝";
        }
        return "未知";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewOrderCode() {
        return reviewOrderCode;
    }

    public void setReviewOrderCode(String reviewOrderCode) {
        this.reviewOrderCode = reviewOrderCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevelName() {
        return riskLevelName;
    }

    public void setRiskLevelName(String riskLevelName) {
        this.riskLevelName = riskLevelName;
    }

    public String getRiskResult() {
        return riskResult;
    }

    public void setRiskResult(String riskResult) {
        this.riskResult = riskResult;
    }

    public String getRiskResultName() {
        return riskResultName;
    }

    public void setRiskResultName(String riskResultName) {
        this.riskResultName = riskResultName;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewStatusName() {
        return reviewStatusName;
    }

    public void setReviewStatusName(String reviewStatusName) {
        this.reviewStatusName = reviewStatusName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
