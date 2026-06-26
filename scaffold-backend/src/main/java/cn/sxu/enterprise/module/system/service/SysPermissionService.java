package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.module.system.vo.SysPermissionVO;

public interface SysPermissionService {

    SysPermissionVO getUserPermissions(Long userId);
}