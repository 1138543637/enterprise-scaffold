package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.dto.AiopsAlertGenerateRequest;
import cn.sxu.enterprise.module.aiops.service.AiopsAlertEventService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aiops/alert-events")
public class AiopsAlertEventController {
    private final AiopsAlertEventService aiopsAlertEventService;
    public AiopsAlertEventController(AiopsAlertEventService aiopsAlertEventService) { this.aiopsAlertEventService = aiopsAlertEventService; }
    @OperLog(title = "AIOps告警事件", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsAlertEventPageVO>> page(AiopsAlertEventPageQuery query) { return ApiResult.success(aiopsAlertEventService.pageAlertEvents(query)); }
    @OperLog(title = "AIOps告警事件", businessType = "生成告警事件")
    @PostMapping("/generate")
    public ApiResult<?> generate(@Valid @RequestBody(required = false) AiopsAlertGenerateRequest request) { return ApiResult.success(aiopsAlertEventService.generate(request)); }
}
