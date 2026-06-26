package cn.sxu.enterprise.module.system.vo;

import java.util.ArrayList;
import java.util.List;

public class SysPermissionVO {

    private Long userId;

    private List<String> roleKeys = new ArrayList<>();

    private List<String> permissions = new ArrayList<>();

    private List<SysMenuTreeVO> menus = new ArrayList<>();

    // getter/setter 用 IDEA 自动生成

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoleKeys() {
        return roleKeys;
    }

    public void setRoleKeys(List<String> roleKeys) {
        this.roleKeys = roleKeys;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<SysMenuTreeVO> getMenus() {
        return menus;
    }

    public void setMenus(List<SysMenuTreeVO> menus) {
        this.menus = menus;
    }
}