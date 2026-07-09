package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamSecurityPolicyService;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyPageVO;
import cn.sxu.enterprise.module.iam.vo.IamSecurityPolicyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/iam/security-policies")
public class IamSecurityPolicyController {

    private final IamSecurityPolicyService iamSecurityPolicyService;

    @OperLog(title = "统一身份认证与安全审计-安全策略配置", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<IamSecurityPolicyPageVO>> page(IamSecurityPolicyPageQuery query) {
        return ApiResult.success(iamSecurityPolicyService.pageSecurityPolicies(query));
    }

    @OperLog(title = "统一身份认证与安全审计-安全策略配置", businessType = "启用策略")
    @PostMapping("/{id}/enable")
    public ApiResult<Boolean> enable(@PathVariable Long id) {
        return ApiResult.success(iamSecurityPolicyService.enablePolicy(id));
    }

    @OperLog(title = "统一身份认证与安全审计-安全策略配置", businessType = "停用策略")
    @PostMapping("/{id}/disable")
    public ApiResult<Boolean> disable(@PathVariable Long id) {
        return ApiResult.success(iamSecurityPolicyService.disablePolicy(id));
    }

    @OperLog(title = "统一身份认证与安全审计-安全策略配置", businessType = "更新策略")
    @PostMapping("/{id}/update-config")
    public ApiResult<IamSecurityPolicyPageVO> updateConfig(@PathVariable Long id,
                                                           @RequestBody IamSecurityPolicyUpdateRequest request) {
        return ApiResult.success(iamSecurityPolicyService.updatePolicy(id, request));
    }
}
