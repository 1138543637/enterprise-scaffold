package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.service.SysOperLogService;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysOperLogController {

    private final SysOperLogService sysOperLogService;

    public SysOperLogController(SysOperLogService sysOperLogService) {
        this.sysOperLogService = sysOperLogService;
    }

    @OperLog(title = "操作日志", businessType = "分页查询")
    @GetMapping("/api/system/oper-logs/page")
    public ApiResult<PageResult<SysOperLogPageVO>> pageOperLogs(SysOperLogPageQuery query) {
        return ApiResult.success(sysOperLogService.pageOperLogs(query));
    }
}