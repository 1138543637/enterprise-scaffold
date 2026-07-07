package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.dto.RiskReviewApproveRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewOrderCreateRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewRejectRequest;
import cn.sxu.enterprise.module.risk.service.RiskReviewOrderService;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskReviewSummaryVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk/review-orders")
public class RiskReviewOrderController {

    @Resource
    private RiskReviewOrderService riskReviewOrderService;

    @GetMapping("/page")
    @OperLog(title = "银行风控-人工审核", businessType = "分页查询")
    public ApiResult<PageResult<RiskReviewOrderPageVO>> pageReviewOrders(RiskReviewOrderPageQuery query) {
        return ApiResult.success(riskReviewOrderService.pageReviewOrders(query));
    }

    @GetMapping("/summary")
    @OperLog(title = "银行风控-人工审核", businessType = "汇总统计")
    public ApiResult<RiskReviewSummaryVO> summary() {
        return ApiResult.success(riskReviewOrderService.summary());
    }

    @PostMapping("/create-from-transaction")
    @OperLog(title = "银行风控-人工审核", businessType = "生成审核单")
    public ApiResult<List<RiskReviewOrderPageVO>> createFromTransaction(
            @Valid @RequestBody(required = false) RiskReviewOrderCreateRequest request) {
        RiskReviewOrderCreateRequest safeRequest = request == null ? new RiskReviewOrderCreateRequest() : request;
        return ApiResult.success(riskReviewOrderService.createFromTransaction(safeRequest));
    }

    @PostMapping("/{id}/approve")
    @OperLog(title = "银行风控-人工审核", businessType = "审核通过")
    public ApiResult<RiskReviewOrderPageVO> approve(
            @PathVariable Long id,
            @Valid @RequestBody RiskReviewApproveRequest request) {
        return ApiResult.success(riskReviewOrderService.approve(id, request));
    }

    @PostMapping("/{id}/reject")
    @OperLog(title = "银行风控-人工审核", businessType = "审核拒绝")
    public ApiResult<RiskReviewOrderPageVO> reject(
            @PathVariable Long id,
            @Valid @RequestBody RiskReviewRejectRequest request) {
        return ApiResult.success(riskReviewOrderService.reject(id, request));
    }
}
