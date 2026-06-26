package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysUser;
import cn.sxu.enterprise.module.system.vo.SysUserPageQuery;
import cn.sxu.enterprise.module.system.vo.SysUserPageVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {

    PageResult<SysUserPageVO> pageUsers(SysUserPageQuery query);
}