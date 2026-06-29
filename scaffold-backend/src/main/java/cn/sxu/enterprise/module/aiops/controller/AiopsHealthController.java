package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aiops")
public class AiopsHealthController {

    @OperLog(title = "AIOps智能运维", businessType = "模块健康检查")
    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold aiops module running");
    }
}