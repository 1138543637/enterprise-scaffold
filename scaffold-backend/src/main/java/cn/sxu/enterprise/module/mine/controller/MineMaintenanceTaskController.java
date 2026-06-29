package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskHandleRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskPlanRequest;
import cn.sxu.enterprise.module.mine.service.MineMaintenanceTaskService;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskSummaryVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/maintenance-tasks")
public class MineMaintenanceTaskController {

    private final MineMaintenanceTaskService mineMaintenanceTaskService;

    public MineMaintenanceTaskController(MineMaintenanceTaskService mineMaintenanceTaskService) {
        this.mineMaintenanceTaskService = mineMaintenanceTaskService;
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineMaintenanceTaskPageVO>> page(MineMaintenanceTaskPageQuery query) {
        return ApiResult.success(mineMaintenanceTaskService.pageMaintenanceTasks(query));
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<MineMaintenanceTaskSummaryVO> summary() {
        return ApiResult.success(mineMaintenanceTaskService.getSummary());
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "健康风险生成任务")
    @PostMapping("/create-from-device-health")
    public ApiResult<MineMaintenanceTaskPageVO> createFromDeviceHealth(
            @Valid @RequestBody MineMaintenanceTaskCreateRequest request) {
        return ApiResult.success(mineMaintenanceTaskService.createFromDeviceHealth(request));
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "任务安排")
    @PostMapping("/{id}/plan")
    public ApiResult<MineMaintenanceTaskPageVO> plan(@PathVariable Long id,
                                                     @Valid @RequestBody MineMaintenanceTaskPlanRequest request) {
        return ApiResult.success(mineMaintenanceTaskService.plan(id, request));
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "任务处理")
    @PostMapping("/{id}/handle")
    public ApiResult<MineMaintenanceTaskPageVO> handle(@PathVariable Long id,
                                                       @Valid @RequestBody MineMaintenanceTaskHandleRequest request) {
        return ApiResult.success(mineMaintenanceTaskService.handle(id, request));
    }

    @OperLog(title = "智能矿山-预测性维护", businessType = "任务关闭")
    @PostMapping("/{id}/close")
    public ApiResult<MineMaintenanceTaskPageVO> close(@PathVariable Long id,
                                                      @Valid @RequestBody MineMaintenanceTaskCloseRequest request) {
        return ApiResult.success(mineMaintenanceTaskService.close(id, request));
    }
}
