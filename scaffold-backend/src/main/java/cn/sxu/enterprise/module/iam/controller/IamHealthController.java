package cn.sxu.enterprise.module.iam.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iam")
public class IamHealthController {

    @OperLog(title = "统一身份认证与安全审计", businessType = "模块健康检查")
    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold iam module running");
    }
}
