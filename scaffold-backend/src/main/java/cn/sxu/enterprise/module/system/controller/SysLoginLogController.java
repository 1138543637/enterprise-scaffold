package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.service.SysLoginLogService;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLoginLogController {

    private final SysLoginLogService sysLoginLogService;

    public SysLoginLogController(SysLoginLogService sysLoginLogService) {
        this.sysLoginLogService = sysLoginLogService;
    }

    @OperLog(title = "登录日志", businessType = "分页查询")
    @GetMapping("/api/system/login-logs/page")
    public ApiResult<PageResult<SysLoginLogPageVO>> pageLoginLogs(SysLoginLogPageQuery query) {
        return ApiResult.success(sysLoginLogService.pageLoginLogs(query));
    }
}