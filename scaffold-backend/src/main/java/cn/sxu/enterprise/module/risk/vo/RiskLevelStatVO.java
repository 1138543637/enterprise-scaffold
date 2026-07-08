package cn.sxu.enterprise.module.risk.vo;

public class RiskLevelStatVO {

    private Integer riskLevel;
    private String riskLevelName;
    private Long total;

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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
