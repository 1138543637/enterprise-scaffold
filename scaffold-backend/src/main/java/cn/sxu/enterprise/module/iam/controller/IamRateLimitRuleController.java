package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamRateLimitRuleService;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageQuery;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageVO;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateRequest;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iam/rate-limit-rules")
@RequiredArgsConstructor
public class IamRateLimitRuleController {

    private final IamRateLimitRuleService iamRateLimitRuleService;

    @OperLog(title = "统一身份认证与安全审计-接口限流", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<IamRateLimitRulePageVO>> page(IamRateLimitRulePageQuery query) {
        return ApiResult.success(iamRateLimitRuleService.pageRateLimitRules(query));
    }

    @OperLog(title = "统一身份认证与安全审计-接口限流", businessType = "启用规则")
    @PostMapping("/{id}/enable")
    public ApiResult<Boolean> enable(@PathVariable Long id) {
        return ApiResult.success(iamRateLimitRuleService.enableRule(id));
    }

    @OperLog(title = "统一身份认证与安全审计-接口限流", businessType = "停用规则")
    @PostMapping("/{id}/disable")
    public ApiResult<Boolean> disable(@PathVariable Long id) {
        return ApiResult.success(iamRateLimitRuleService.disableRule(id));
    }

    @OperLog(title = "统一身份认证与安全审计-接口限流", businessType = "模拟检测")
    @PostMapping("/simulate")
    public ApiResult<IamRateLimitRuleSimulateVO> simulate(
        @RequestBody(required = false) IamRateLimitRuleSimulateRequest request
    ) {
        return ApiResult.success(iamRateLimitRuleService.simulate(request));
    }
}
