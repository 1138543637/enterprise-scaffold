package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamSecurityDashboardService;
import cn.sxu.enterprise.module.iam.vo.IamSecurityDashboardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/iam/security-dashboard")
public class IamSecurityDashboardController {

    private final IamSecurityDashboardService iamSecurityDashboardService;

    @OperLog(title = "统一身份认证与安全审计-IAM安全看板", businessType = "安全总览")
    @GetMapping("/overview")
    public ApiResult<IamSecurityDashboardVO> overview() {
        return ApiResult.success(iamSecurityDashboardService.getDashboard());
    }
}
