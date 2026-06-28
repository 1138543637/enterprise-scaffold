package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.dto.MineSensorDataSimulateRequest;
import cn.sxu.enterprise.module.mine.service.MineSensorDataService;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/sensor-data")
public class MineSensorDataController {

    private final MineSensorDataService mineSensorDataService;

    public MineSensorDataController(MineSensorDataService mineSensorDataService) {
        this.mineSensorDataService = mineSensorDataService;
    }

    @OperLog(title = "智能矿山-传感器数据", businessType = "模拟生成")
    @PostMapping("/simulate")
    public ApiResult<?> simulate(@Valid @RequestBody(required = false) MineSensorDataSimulateRequest request) {
        return ApiResult.success(mineSensorDataService.simulate(request));
    }

    @OperLog(title = "智能矿山-传感器数据", businessType = "查询最新数据")
    @GetMapping("/latest")
    public ApiResult<?> latest(MineSensorDataPageQuery query) {
        return ApiResult.success(mineSensorDataService.latest(query));
    }

    @OperLog(title = "智能矿山-传感器数据", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineSensorDataVO>> page(MineSensorDataPageQuery query) {
        return ApiResult.success(mineSensorDataService.pageSensorData(query));
    }
}