package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubQualityCheckRequest;
import cn.sxu.enterprise.module.datahub.service.DatahubQualityService;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/datahub")
@RequiredArgsConstructor
public class DatahubQualityController {

    private final DatahubQualityService datahubQualityService;

    /**
     * 如果你现有项目的 @OperLog 注解有固定写法，请参考 DatahubMetadataController 中同类方法补上。
     * 本文件不写死注解参数，是为了避免不同版本 @OperLog 属性名不一致导致编译失败。
     */
    @GetMapping("/quality-rules/page")
    public ApiResult<PageResult<DatahubQualityRulePageVO>> pageRules(DatahubQualityRulePageQuery query) {
        return ApiResult.success(datahubQualityService.pageRules(query));
    }

    @PostMapping("/quality-results/check")
    public ApiResult<List<DatahubQualityResultPageVO>> check(@RequestBody DatahubQualityCheckRequest request) {
        return ApiResult.success(datahubQualityService.check(request));
    }

    @GetMapping("/quality-results/page")
    public ApiResult<PageResult<DatahubQualityResultPageVO>> pageResults(DatahubQualityResultPageQuery query) {
        return ApiResult.success(datahubQualityService.pageResults(query));
    }
}
