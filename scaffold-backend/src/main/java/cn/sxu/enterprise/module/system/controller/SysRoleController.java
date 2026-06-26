package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.module.system.service.SysRoleService;
import cn.sxu.enterprise.module.system.vo.SysRolePageQuery;
import cn.sxu.enterprise.module.system.vo.SysRolePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/api/system/roles/page")
    public ApiResult<PageResult<SysRolePageVO>> pageRoles(SysRolePageQuery query) {
        return ApiResult.success(sysRoleService.pageRoles(query));
    }
}