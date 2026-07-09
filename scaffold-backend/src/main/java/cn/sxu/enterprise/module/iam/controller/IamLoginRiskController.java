package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.iam.service.IamLoginRiskService;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * IAM 异常登录检测 Controller。
 */
@RestController
@RequestMapping("/api/iam/login-risks")
public class IamLoginRiskController {

    @Resource
    private IamLoginRiskService iamLoginRiskService;

    @OperLog(title = "统一身份认证与安全审计-异常登录检测", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<IamLoginRiskPageVO>> page(IamLoginRiskPageQuery query) {
        return ApiResult.success(iamLoginRiskService.pageLoginRisks(query));
    }

    @OperLog(title = "统一身份认证与安全审计-异常登录检测", businessType = "风险检测")
    @PostMapping("/detect")
    public ApiResult<List<IamLoginRiskPageVO>> detect() {
        return ApiResult.success(iamLoginRiskService.detectLoginRisks());
    }
}
