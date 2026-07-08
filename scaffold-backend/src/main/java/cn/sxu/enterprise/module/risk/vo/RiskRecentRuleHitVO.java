package cn.sxu.enterprise.module.risk.vo;

import cn.sxu.enterprise.module.risk.entity.RiskRuleHit;

import java.time.LocalDateTime;

public class RiskRecentRuleHitVO {

    private Long id;
    private String hitCode;
    private String transactionNo;
    private String accountNo;
    private String customerName;
    private String ruleCode;
    private String ruleName;
    private String ruleType;
    private String ruleTypeName;
    private Integer riskLevel;
    private String riskLevelName;
    private Integer riskScore;
    private LocalDateTime hitTime;

    public static RiskRecentRuleHitVO fromEntity(RiskRuleHit entity) {
        RiskRecentRuleHitVO vo = new RiskRecentRuleHitVO();
        vo.setId(entity.getId());
        vo.setHitCode(entity.getHitCode());
        vo.setTransactionNo(entity.getTransactionNo());
        vo.setAccountNo(entity.getAccountNo());
        vo.setCustomerName(entity.getCustomerName());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setRuleType(entity.getRuleType());
        vo.setRuleTypeName(ruleTypeName(entity.getRuleType()));
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setRiskLevelName(riskLevelName(entity.getRiskLevel()));
        vo.setRiskScore(entity.getRiskScore());
        vo.setHitTime(entity.getHitTime());
        return vo;
    }

    private static String ruleTypeName(String ruleType) {
        if ("AMOUNT".equals(ruleType)) {
            return "大额交易";
        }
        if ("FREQUENCY".equals(ruleType)) {
            return "高频交易";
        }
        if ("LOCATION".equals(ruleType)) {
            return "异地交易";
        }
        if ("DEVICE".equals(ruleType)) {
            return "异常设备";
        }
        if ("TIME".equals(ruleType)) {
            return "夜间交易";
        }
        if ("BLACKLIST".equals(ruleType)) {
            return "黑名单";
        }
        return ruleType;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHitCode() {
        return hitCode;
    }

    public void setHitCode(String hitCode) {
        this.hitCode = hitCode;
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

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleTypeName() {
        return ruleTypeName;
    }

    public void setRuleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
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

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public LocalDateTime getHitTime() {
        return hitTime;
    }

    public void setHitTime(LocalDateTime hitTime) {
        this.hitTime = hitTime;
    }
}
