package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineDeviceService;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/devices")
public class MineDeviceController {

    private final MineDeviceService mineDeviceService;

    public MineDeviceController(MineDeviceService mineDeviceService) {
        this.mineDeviceService = mineDeviceService;
    }

    @OperLog(title = "智能矿山-设备台账", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineDevicePageVO>> page(MineDevicePageQuery query) {
        return ApiResult.success(mineDeviceService.pageDevices(query));
    }
}