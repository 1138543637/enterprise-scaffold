package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.module.system.service.SysUserService;
import cn.sxu.enterprise.module.system.vo.SysUserPageQuery;
import cn.sxu.enterprise.module.system.vo.SysUserPageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.sxu.enterprise.common.web.annotation.OperLog;

@RestController
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @OperLog(title = "用户管理", businessType = "分页查询")
    @GetMapping("/api/system/users/page")
    public ApiResult<PageResult<SysUserPageVO>> pageUsers(SysUserPageQuery query) {
        return ApiResult.success(sysUserService.pageUsers(query));
    }
}