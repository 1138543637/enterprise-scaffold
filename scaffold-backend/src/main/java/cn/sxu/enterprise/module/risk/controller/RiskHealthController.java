package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk")
public class RiskHealthController {

    @OperLog(title = "银行风控", businessType = "模块健康检查")
    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold risk module running");
    }
}
