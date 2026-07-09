package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.datahub.dto.DatahubMaskPreviewRequest;
import cn.sxu.enterprise.module.datahub.dto.DatahubSensitiveScanRequest;
import cn.sxu.enterprise.module.datahub.service.DatahubSensitiveService;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatahubSensitiveController {

    private final DatahubSensitiveService datahubSensitiveService;

    public DatahubSensitiveController(DatahubSensitiveService datahubSensitiveService) {
        this.datahubSensitiveService = datahubSensitiveService;
    }

    @OperLog(title = "数据治理-敏感字段", businessType = "敏感字段识别")
    @PostMapping("/api/datahub/sensitive-fields/scan")
    public ApiResult<List<DatahubSensitiveFieldPageVO>> scanSensitiveFields(
            @Valid @RequestBody(required = false) DatahubSensitiveScanRequest request) {
        return ApiResult.success(datahubSensitiveService.scanSensitiveFields(request));
    }

    @OperLog(title = "数据治理-敏感字段", businessType = "分页查询")
    @GetMapping("/api/datahub/sensitive-fields/page")
    public ApiResult<PageResult<DatahubSensitiveFieldPageVO>> pageSensitiveFields(DatahubSensitiveFieldPageQuery query) {
        return ApiResult.success(datahubSensitiveService.pageSensitiveFields(query));
    }

    @OperLog(title = "数据治理-脱敏规则", businessType = "分页查询")
    @GetMapping("/api/datahub/mask-rules/page")
    public ApiResult<PageResult<DatahubMaskRulePageVO>> pageMaskRules(DatahubMaskRulePageQuery query) {
        return ApiResult.success(datahubSensitiveService.pageMaskRules(query));
    }

    @OperLog(title = "数据治理-脱敏结果", businessType = "脱敏预览")
    @PostMapping("/api/datahub/mask-results/preview")
    public ApiResult<DatahubMaskResultPageVO> previewMask(@Valid @RequestBody DatahubMaskPreviewRequest request) {
        return ApiResult.success(datahubSensitiveService.previewMask(request));
    }

    @OperLog(title = "数据治理-脱敏结果", businessType = "分页查询")
    @GetMapping("/api/datahub/mask-results/page")
    public ApiResult<PageResult<DatahubMaskResultPageVO>> pageMaskResults(DatahubMaskResultPageQuery query) {
        return ApiResult.success(datahubSensitiveService.pageMaskResults(query));
    }
}
