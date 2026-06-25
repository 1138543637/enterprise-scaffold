package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold backend running");
    }
}