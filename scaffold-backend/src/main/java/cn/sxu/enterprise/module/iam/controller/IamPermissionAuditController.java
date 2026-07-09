package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamPermissionAuditService;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditPageVO;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditReviewRequest;
import cn.sxu.enterprise.module.iam.vo.IamPermissionAuditSimulateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/iam/permission-audits")
public class IamPermissionAuditController {

    private final IamPermissionAuditService iamPermissionAuditService;

    @OperLog(title = "统一身份认证与安全审计-权限审计", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<IamPermissionAuditPageVO>> page(IamPermissionAuditPageQuery query) {
        return ApiResult.success(iamPermissionAuditService.pagePermissionAudits(query));
    }

    @OperLog(title = "统一身份认证与安全审计-权限审计", businessType = "模拟审计记录")
    @PostMapping("/simulate")
    public ApiResult<IamPermissionAuditPageVO> simulate(@RequestBody IamPermissionAuditSimulateRequest request) {
        return ApiResult.success(iamPermissionAuditService.simulatePermissionAudit(request));
    }

    @OperLog(title = "统一身份认证与安全审计-权限审计", businessType = "复核审计记录")
    @PostMapping("/{id}/review")
    public ApiResult<IamPermissionAuditPageVO> review(@PathVariable Long id,
                                                       @RequestBody IamPermissionAuditReviewRequest request) {
        return ApiResult.success(iamPermissionAuditService.reviewPermissionAudit(id, request));
    }
}
