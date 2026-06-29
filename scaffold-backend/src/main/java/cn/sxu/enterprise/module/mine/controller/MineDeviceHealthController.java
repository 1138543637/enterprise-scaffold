package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineDeviceHealthService;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/device-health")
public class MineDeviceHealthController {

    private final MineDeviceHealthService mineDeviceHealthService;

    public MineDeviceHealthController(MineDeviceHealthService mineDeviceHealthService) {
        this.mineDeviceHealthService = mineDeviceHealthService;
    }

    @OperLog(title = "智能矿山-设备健康", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineDeviceHealthVO>> page(MineDeviceHealthPageQuery query) {
        return ApiResult.success(mineDeviceHealthService.pageDeviceHealth(query));
    }

    @OperLog(title = "智能矿山-设备健康", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<MineDeviceHealthSummaryVO> summary() {
        return ApiResult.success(mineDeviceHealthService.getSummary());
    }
}
