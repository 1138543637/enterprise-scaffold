package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.service.AiopsResourceService;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aiops/resources")
public class AiopsResourceController {

    private final AiopsResourceService aiopsResourceService;

    public AiopsResourceController(AiopsResourceService aiopsResourceService) {
        this.aiopsResourceService = aiopsResourceService;
    }

    @OperLog(title = "AIOps资源管理", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsResourcePageVO>> page(AiopsResourcePageQuery query) {
        return ApiResult.success(aiopsResourceService.pageResources(query));
    }
}
