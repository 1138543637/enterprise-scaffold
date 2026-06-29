package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineDashboardService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentAlarmVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentWorkOrderVO;
import cn.sxu.enterprise.module.mine.vo.MineSensorTypeStatVO;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderStatusStatVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mine/dashboard")
public class MineDashboardController {

    private final MineDashboardService mineDashboardService;

    public MineDashboardController(MineDashboardService mineDashboardService) {
        this.mineDashboardService = mineDashboardService;
    }

    @OperLog(title = "智能矿山-看板", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<MineDashboardSummaryVO> summary() {
        return ApiResult.success(mineDashboardService.getSummary());
    }

    @OperLog(title = "智能矿山-看板", businessType = "告警级别统计")
    @GetMapping("/alarm-level-stats")
    public ApiResult<List<MineAlarmLevelStatVO>> alarmLevelStats() {
        return ApiResult.success(mineDashboardService.getAlarmLevelStats());
    }

    @OperLog(title = "智能矿山-看板", businessType = "传感器类型统计")
    @GetMapping("/sensor-type-stats")
    public ApiResult<List<MineSensorTypeStatVO>> sensorTypeStats() {
        return ApiResult.success(mineDashboardService.getSensorTypeStats());
    }

    @OperLog(title = "智能矿山-看板", businessType = "工单状态统计")
    @GetMapping("/work-order-status-stats")
    public ApiResult<List<MineWorkOrderStatusStatVO>> workOrderStatusStats() {
        return ApiResult.success(mineDashboardService.getWorkOrderStatusStats());
    }

    @OperLog(title = "智能矿山-看板", businessType = "最近告警查询")
    @GetMapping("/recent-alarms")
    public ApiResult<List<MineRecentAlarmVO>> recentAlarms() {
        return ApiResult.success(mineDashboardService.getRecentAlarms());
    }

    @OperLog(title = "智能矿山-看板", businessType = "最近工单查询")
    @GetMapping("/recent-work-orders")
    public ApiResult<List<MineRecentWorkOrderVO>> recentWorkOrders() {
        return ApiResult.success(mineDashboardService.getRecentWorkOrders());
    }
}
