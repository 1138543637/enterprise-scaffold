package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineSensorService;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/sensors")
public class MineSensorController {

    private final MineSensorService mineSensorService;

    public MineSensorController(MineSensorService mineSensorService) {
        this.mineSensorService = mineSensorService;
    }

    @OperLog(title = "智能矿山-传感器台账", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineSensorPageVO>> page(MineSensorPageQuery query) {
        return ApiResult.success(mineSensorService.pageSensors(query));
    }
}