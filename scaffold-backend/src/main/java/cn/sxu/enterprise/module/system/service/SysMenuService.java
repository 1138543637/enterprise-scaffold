package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.module.system.entity.SysMenu;
import cn.sxu.enterprise.module.system.vo.SysMenuTreeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuTreeVO> menuTree();
}