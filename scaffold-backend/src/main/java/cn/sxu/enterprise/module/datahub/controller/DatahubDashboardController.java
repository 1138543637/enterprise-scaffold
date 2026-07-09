package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.datahub.service.DatahubDashboardService;
import cn.sxu.enterprise.module.datahub.vo.DatahubDashboardSummaryVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubDatasourceTypeStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultStatVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentApiPublishVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubRecentQualityResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveTypeStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/datahub/dashboard")
public class DatahubDashboardController {

    private final DatahubDashboardService datahubDashboardService;

    @OperLog(title = "数据治理-看板", businessType = "汇总统计")
    @GetMapping("/summary")
    public ApiResult<DatahubDashboardSummaryVO> summary() {
        return ApiResult.success(datahubDashboardService.getSummary());
    }

    @OperLog(title = "数据治理-看板", businessType = "数据源类型统计")
    @GetMapping("/datasource-type-stats")
    public ApiResult<List<DatahubDatasourceTypeStatVO>> datasourceTypeStats() {
        return ApiResult.success(datahubDashboardService.getDatasourceTypeStats());
    }

    @OperLog(title = "数据治理-看板", businessType = "质量结果统计")
    @GetMapping("/quality-result-stats")
    public ApiResult<List<DatahubQualityResultStatVO>> qualityResultStats() {
        return ApiResult.success(datahubDashboardService.getQualityResultStats());
    }

    @OperLog(title = "数据治理-看板", businessType = "敏感类型统计")
    @GetMapping("/sensitive-type-stats")
    public ApiResult<List<DatahubSensitiveTypeStatVO>> sensitiveTypeStats() {
        return ApiResult.success(datahubDashboardService.getSensitiveTypeStats());
    }

    @OperLog(title = "数据治理-看板", businessType = "最近质量结果")
    @GetMapping("/recent-quality-results")
    public ApiResult<List<DatahubRecentQualityResultVO>> recentQualityResults() {
        return ApiResult.success(datahubDashboardService.getRecentQualityResults());
    }

    @OperLog(title = "数据治理-看板", businessType = "最近API发布")
    @GetMapping("/recent-apis")
    public ApiResult<List<DatahubRecentApiPublishVO>> recentApis() {
        return ApiResult.success(datahubDashboardService.getRecentApis());
    }
}
