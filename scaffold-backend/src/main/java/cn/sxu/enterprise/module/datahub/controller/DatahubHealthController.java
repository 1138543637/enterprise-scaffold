package cn.sxu.enterprise.module.datahub.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datahub")
public class DatahubHealthController {

    @OperLog(title = "数据治理", businessType = "模块健康检查")
    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold datahub module running");
    }
}