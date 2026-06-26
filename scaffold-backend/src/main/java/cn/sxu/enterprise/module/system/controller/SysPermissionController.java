package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.module.system.service.SysPermissionService;
import cn.sxu.enterprise.module.system.vo.SysPermissionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysPermissionController {

    private final SysPermissionService sysPermissionService;

    public SysPermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    @GetMapping("/api/system/users/{userId}/permissions")
    public ApiResult<SysPermissionVO> getUserPermissions(@PathVariable Long userId) {
        return ApiResult.success(sysPermissionService.getUserPermissions(userId));
    }
}