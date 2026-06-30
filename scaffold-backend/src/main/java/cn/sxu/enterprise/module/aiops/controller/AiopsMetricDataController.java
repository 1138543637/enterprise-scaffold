package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.dto.AiopsMetricDataSimulateRequest;
import cn.sxu.enterprise.module.aiops.service.AiopsMetricDataService;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aiops/metric-data")
public class AiopsMetricDataController {

    private final AiopsMetricDataService aiopsMetricDataService;

    public AiopsMetricDataController(AiopsMetricDataService aiopsMetricDataService) {
        this.aiopsMetricDataService = aiopsMetricDataService;
    }

    @OperLog(title = "AIOps指标数据", businessType = "模拟生成")
    @PostMapping("/simulate")
    public ApiResult<?> simulate(@Valid @RequestBody(required = false) AiopsMetricDataSimulateRequest request) {
        return ApiResult.success(aiopsMetricDataService.simulate(request));
    }

    @OperLog(title = "AIOps指标数据", businessType = "查询最新数据")
    @GetMapping("/latest")
    public ApiResult<?> latest(AiopsMetricDataPageQuery query) {
        return ApiResult.success(aiopsMetricDataService.latest(query));
    }

    @OperLog(title = "AIOps指标数据", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsMetricDataVO>> page(AiopsMetricDataPageQuery query) {
        return ApiResult.success(aiopsMetricDataService.pageMetricData(query));
    }
}
