package cn.sxu.enterprise.module.system.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.module.system.service.SysMenuService;
import cn.sxu.enterprise.module.system.vo.SysMenuTreeVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping("/api/system/menus/tree")
    public ApiResult<List<SysMenuTreeVO>> menuTree() {
        return ApiResult.success(sysMenuService.menuTree());
    }
}