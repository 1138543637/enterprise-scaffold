package cn.sxu.enterprise.module.risk.dto;

import jakarta.validation.constraints.NotBlank;

public class RiskReviewApproveRequest {

    private Long reviewerUserId;

    private String reviewerUsername;

    @NotBlank(message = "审核意见不能为空")
    private String reviewResult;

    public Long getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }
}
