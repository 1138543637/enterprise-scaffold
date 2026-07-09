package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamRiskClosureService;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskHandleRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditClosureRequest;
import cn.sxu.enterprise.module.iam.vo.IamRiskClosureSummaryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IAM 风险闭环处理 Controller。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/iam/risk-closures")
public class IamRiskClosureController {

    private final IamRiskClosureService iamRiskClosureService;

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "闭环统计")
    @GetMapping("/summary")
    public ApiResult<IamRiskClosureSummaryVO> summary() {
        return ApiResult.success(iamRiskClosureService.getSummary());
    }

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "确认登录风险")
    @PostMapping("/login-risks/{id}/confirm")
    public ApiResult<Boolean> confirmLoginRisk(@PathVariable Long id,
                                               @RequestBody(required = false) IamLoginRiskHandleRequest request) {
        return ApiResult.success(iamRiskClosureService.confirmLoginRisk(id, request));
    }

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "忽略登录风险")
    @PostMapping("/login-risks/{id}/ignore")
    public ApiResult<Boolean> ignoreLoginRisk(@PathVariable Long id,
                                              @RequestBody(required = false) IamLoginRiskHandleRequest request) {
        return ApiResult.success(iamRiskClosureService.ignoreLoginRisk(id, request));
    }

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "关闭登录风险")
    @PostMapping("/login-risks/{id}/close")
    public ApiResult<Boolean> closeLoginRisk(@PathVariable Long id,
                                             @RequestBody(required = false) IamLoginRiskHandleRequest request) {
        return ApiResult.success(iamRiskClosureService.closeLoginRisk(id, request));
    }

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "复核权限审计")
    @PostMapping("/permission-audits/{id}/review")
    public ApiResult<Boolean> reviewPermissionAudit(@PathVariable Long id,
                                                    @RequestBody(required = false) IamPermissionAuditClosureRequest request) {
        return ApiResult.success(iamRiskClosureService.reviewPermissionAudit(id, request));
    }

    @OperLog(title = "统一身份认证与安全审计-风险闭环", businessType = "忽略权限审计")
    @PostMapping("/permission-audits/{id}/ignore")
    public ApiResult<Boolean> ignorePermissionAudit(@PathVariable Long id,
                                                    @RequestBody(required = false) IamPermissionAuditClosureRequest request) {
        return ApiResult.success(iamRiskClosureService.ignorePermissionAudit(id, request));
    }
}
