package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.module.system.entity.SysMenu;
import cn.sxu.enterprise.module.system.entity.SysRole;
import cn.sxu.enterprise.module.system.entity.SysRoleMenu;
import cn.sxu.enterprise.module.system.entity.SysUserRole;
import cn.sxu.enterprise.module.system.mapper.SysMenuMapper;
import cn.sxu.enterprise.module.system.mapper.SysRoleMapper;
import cn.sxu.enterprise.module.system.mapper.SysRoleMenuMapper;
import cn.sxu.enterprise.module.system.mapper.SysUserRoleMapper;
import cn.sxu.enterprise.module.system.service.SysPermissionService;
import cn.sxu.enterprise.module.system.vo.SysMenuTreeVO;
import cn.sxu.enterprise.module.system.vo.SysPermissionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;

    public SysPermissionServiceImpl(SysUserRoleMapper sysUserRoleMapper,
                                    SysRoleMapper sysRoleMapper,
                                    SysRoleMenuMapper sysRoleMenuMapper,
                                    SysMenuMapper sysMenuMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public SysPermissionVO getUserPermissions(Long userId) {
        SysPermissionVO vo = new SysPermissionVO();
        vo.setUserId(userId);

        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );

        Set<Long> roleIds = new LinkedHashSet<>();
        for (SysUserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }

        if (roleIds.isEmpty()) {
            return vo;
        }

        List<SysRole> roles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .in(SysRole::getId, roleIds)
                        .eq(SysRole::getStatus, 0)
                        .orderByAsc(SysRole::getRoleSort)
        );

        List<String> roleKeys = roles.stream()
                .map(SysRole::getRoleKey)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        vo.setRoleKeys(roleKeys);

        Set<Long> activeRoleIds = new LinkedHashSet<>();
        for (SysRole role : roles) {
            activeRoleIds.add(role.getId());
        }

        if (activeRoleIds.isEmpty()) {
            return vo;
        }

        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .in(SysRoleMenu::getRoleId, activeRoleIds)
        );

        Set<Long> menuIds = new LinkedHashSet<>();
        for (SysRoleMenu roleMenu : roleMenus) {
            menuIds.add(roleMenu.getMenuId());
        }

        if (menuIds.isEmpty()) {
            return vo;
        }

        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getId, menuIds)
                        .eq(SysMenu::getStatus, 0)
                        .orderByAsc(SysMenu::getParentId)
                        .orderByAsc(SysMenu::getOrderNum)
        );

        List<String> permissions = menus.stream()
                .map(SysMenu::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        vo.setPermissions(permissions);
        vo.setMenus(buildTree(menus));

        return vo;
    }

    private List<SysMenuTreeVO> buildTree(List<SysMenu> menus) {
        Map<Long, SysMenuTreeVO> menuMap = new LinkedHashMap<>();

        for (SysMenu menu : menus) {
            menuMap.put(menu.getId(), SysMenuTreeVO.fromEntity(menu));
        }

        List<SysMenuTreeVO> roots = new ArrayList<>();

        for (SysMenu menu : menus) {
            SysMenuTreeVO current = menuMap.get(menu.getId());
            Long parentId = menu.getParentId();

            if (parentId == null || parentId == 0 || !menuMap.containsKey(parentId)) {
                roots.add(current);
            } else {
                SysMenuTreeVO parent = menuMap.get(parentId);
                parent.getChildren().add(current);
            }
        }

        return roots;
    }
}