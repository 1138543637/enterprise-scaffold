package cn.sxu.enterprise.module.risk.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RiskReviewOrderCreateRequest {

    private Long transactionId;

    @Min(value = 1, message = "最多扫描交易数至少为1")
    @Max(value = 200, message = "最多扫描交易数不能超过200")
    private Integer limit = 50;

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
