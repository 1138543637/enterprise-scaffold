package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能矿山模块健康检查接口。
 *
 * M1-01 阶段只用于验证 mine 模块已经接入 enterprise-scaffold。
 * 本阶段不新增数据库表，不新增前端页面。
 */
@RestController
public class MineHealthController {

    @OperLog(title = "智能矿山", businessType = "模块健康检查")
    @GetMapping("/api/mine/health")
    public ApiResult<String> health() {
        return ApiResult.success("enterprise-scaffold mine module running");
    }
}