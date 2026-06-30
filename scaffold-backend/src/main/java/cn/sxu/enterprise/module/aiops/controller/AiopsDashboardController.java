package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.service.AiopsDashboardService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertLevelStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsDashboardSummaryVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricTrendVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentAlertVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentRootCauseVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentWorkOrderVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourceTypeStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderStatusStatVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aiops/dashboard")
public class AiopsDashboardController {

    private final AiopsDashboardService aiopsDashboardService;

    public AiopsDashboardController(AiopsDashboardService aiopsDashboardService) {
        this.aiopsDashboardService = aiopsDashboardService;
    }

    @OperLog(title = "AIOps综合看板", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<AiopsDashboardSummaryVO> summary() {
        return ApiResult.success(aiopsDashboardService.getSummary());
    }

    @OperLog(title = "AIOps综合看板", businessType = "资源类型统计")
    @GetMapping("/resource-type-stats")
    public ApiResult<List<AiopsResourceTypeStatVO>> resourceTypeStats() {
        return ApiResult.success(aiopsDashboardService.getResourceTypeStats());
    }

    @OperLog(title = "AIOps综合看板", businessType = "告警级别统计")
    @GetMapping("/alert-level-stats")
    public ApiResult<List<AiopsAlertLevelStatVO>> alertLevelStats() {
        return ApiResult.success(aiopsDashboardService.getAlertLevelStats());
    }

    @OperLog(title = "AIOps综合看板", businessType = "工单状态统计")
    @GetMapping("/work-order-status-stats")
    public ApiResult<List<AiopsWorkOrderStatusStatVO>> workOrderStatusStats() {
        return ApiResult.success(aiopsDashboardService.getWorkOrderStatusStats());
    }

    @OperLog(title = "AIOps综合看板", businessType = "指标趋势分析")
    @GetMapping("/metric-trend")
    public ApiResult<List<AiopsMetricTrendVO>> metricTrend() {
        return ApiResult.success(aiopsDashboardService.getMetricTrend());
    }

    @OperLog(title = "AIOps综合看板", businessType = "最近告警查询")
    @GetMapping("/recent-alerts")
    public ApiResult<List<AiopsRecentAlertVO>> recentAlerts() {
        return ApiResult.success(aiopsDashboardService.getRecentAlerts());
    }

    @OperLog(title = "AIOps综合看板", businessType = "最近工单查询")
    @GetMapping("/recent-work-orders")
    public ApiResult<List<AiopsRecentWorkOrderVO>> recentWorkOrders() {
        return ApiResult.success(aiopsDashboardService.getRecentWorkOrders());
    }

    @OperLog(title = "AIOps综合看板", businessType = "最近根因分析查询")
    @GetMapping("/recent-root-causes")
    public ApiResult<List<AiopsRecentRootCauseVO>> recentRootCauses() {
        return ApiResult.success(aiopsDashboardService.getRecentRootCauses());
    }
}
