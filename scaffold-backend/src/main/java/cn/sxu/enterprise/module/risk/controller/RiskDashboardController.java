package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.service.RiskDashboardService;
import cn.sxu.enterprise.module.risk.vo.RiskChannelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskDashboardSummaryVO;
import cn.sxu.enterprise.module.risk.vo.RiskLevelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentReviewOrderVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentRuleHitVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentTransactionVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionTypeStatVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk/dashboard")
public class RiskDashboardController {

    private final RiskDashboardService riskDashboardService;

    public RiskDashboardController(RiskDashboardService riskDashboardService) {
        this.riskDashboardService = riskDashboardService;
    }

    @OperLog(title = "银行风控-风控看板", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<RiskDashboardSummaryVO> summary() {
        return ApiResult.success(riskDashboardService.getSummary());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "渠道统计")
    @GetMapping("/channel-stats")
    public ApiResult<List<RiskChannelStatVO>> channelStats() {
        return ApiResult.success(riskDashboardService.getChannelStats());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "交易类型统计")
    @GetMapping("/transaction-type-stats")
    public ApiResult<List<RiskTransactionTypeStatVO>> transactionTypeStats() {
        return ApiResult.success(riskDashboardService.getTransactionTypeStats());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "风险等级统计")
    @GetMapping("/risk-level-stats")
    public ApiResult<List<RiskLevelStatVO>> riskLevelStats() {
        return ApiResult.success(riskDashboardService.getRiskLevelStats());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "最近交易查询")
    @GetMapping("/recent-transactions")
    public ApiResult<List<RiskRecentTransactionVO>> recentTransactions() {
        return ApiResult.success(riskDashboardService.getRecentTransactions());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "最近规则命中查询")
    @GetMapping("/recent-rule-hits")
    public ApiResult<List<RiskRecentRuleHitVO>> recentRuleHits() {
        return ApiResult.success(riskDashboardService.getRecentRuleHits());
    }

    @OperLog(title = "银行风控-风控看板", businessType = "最近审核单查询")
    @GetMapping("/recent-review-orders")
    public ApiResult<List<RiskRecentReviewOrderVO>> recentReviewOrders() {
        return ApiResult.success(riskDashboardService.getRecentReviewOrders());
    }
}
