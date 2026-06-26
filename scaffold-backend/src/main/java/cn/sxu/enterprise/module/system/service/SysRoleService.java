package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysRole;
import cn.sxu.enterprise.module.system.vo.SysRolePageQuery;
import cn.sxu.enterprise.module.system.vo.SysRolePageVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRolePageVO> pageRoles(SysRolePageQuery query);
}