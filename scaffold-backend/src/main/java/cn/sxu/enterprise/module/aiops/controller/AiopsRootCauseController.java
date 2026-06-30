package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.dto.AiopsRootCauseAnalyzeRequest;
import cn.sxu.enterprise.module.aiops.service.AiopsRootCauseService;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCausePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCauseVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aiops/root-causes")
public class AiopsRootCauseController {

    private final AiopsRootCauseService aiopsRootCauseService;

    public AiopsRootCauseController(AiopsRootCauseService aiopsRootCauseService) {
        this.aiopsRootCauseService = aiopsRootCauseService;
    }

    @OperLog(title = "AIOps根因分析", businessType = "执行分析")
    @PostMapping("/analyze")
    public ApiResult<AiopsRootCauseVO> analyze(@Valid @RequestBody AiopsRootCauseAnalyzeRequest request) {
        return ApiResult.success(aiopsRootCauseService.analyze(request));
    }

    @OperLog(title = "AIOps根因分析", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsRootCauseVO>> page(AiopsRootCausePageQuery query) {
        return ApiResult.success(aiopsRootCauseService.pageRootCauses(query));
    }

    @OperLog(title = "AIOps根因分析", businessType = "详情查询")
    @GetMapping("/{id}")
    public ApiResult<AiopsRootCauseVO> detail(@PathVariable Long id) {
        return ApiResult.success(aiopsRootCauseService.detail(id));
    }
}
