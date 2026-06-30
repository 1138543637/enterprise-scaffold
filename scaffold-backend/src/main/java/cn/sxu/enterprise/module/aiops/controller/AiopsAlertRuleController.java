package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.service.AiopsAlertRuleService;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aiops/alert-rules")
public class AiopsAlertRuleController {
    private final AiopsAlertRuleService aiopsAlertRuleService;
    public AiopsAlertRuleController(AiopsAlertRuleService aiopsAlertRuleService) { this.aiopsAlertRuleService = aiopsAlertRuleService; }
    @OperLog(title = "AIOps告警规则", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsAlertRulePageVO>> page(AiopsAlertRulePageQuery query) {
        return ApiResult.success(aiopsAlertRuleService.pageAlertRules(query));
    }
}
