package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineMaintenanceDashboardService;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceHighRiskDeviceVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenancePriorityStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRecentTaskVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskTrendVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskStatusStatVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mine/maintenance-dashboard")
public class MineMaintenanceDashboardController {

    private final MineMaintenanceDashboardService maintenanceDashboardService;

    public MineMaintenanceDashboardController(MineMaintenanceDashboardService maintenanceDashboardService) {
        this.maintenanceDashboardService = maintenanceDashboardService;
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<MineMaintenanceDashboardSummaryVO> summary() {
        return ApiResult.success(maintenanceDashboardService.getSummary());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "任务状态统计")
    @GetMapping("/task-status-stats")
    public ApiResult<List<MineMaintenanceTaskStatusStatVO>> taskStatusStats() {
        return ApiResult.success(maintenanceDashboardService.getTaskStatusStats());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "优先级统计")
    @GetMapping("/priority-stats")
    public ApiResult<List<MineMaintenancePriorityStatVO>> priorityStats() {
        return ApiResult.success(maintenanceDashboardService.getPriorityStats());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "风险等级统计")
    @GetMapping("/risk-level-stats")
    public ApiResult<List<MineMaintenanceRiskLevelStatVO>> riskLevelStats() {
        return ApiResult.success(maintenanceDashboardService.getRiskLevelStats());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "风险趋势分析")
    @GetMapping("/risk-trend")
    public ApiResult<List<MineMaintenanceRiskTrendVO>> riskTrend() {
        return ApiResult.success(maintenanceDashboardService.getRiskTrend());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "最近任务查询")
    @GetMapping("/recent-tasks")
    public ApiResult<List<MineMaintenanceRecentTaskVO>> recentTasks() {
        return ApiResult.success(maintenanceDashboardService.getRecentTasks());
    }

    @OperLog(title = "智能矿山-维护看板", businessType = "高风险设备查询")
    @GetMapping("/high-risk-devices")
    public ApiResult<List<MineMaintenanceHighRiskDeviceVO>> highRiskDevices() {
        return ApiResult.success(maintenanceDashboardService.getHighRiskDevices());
    }
}
