package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysRole;

import java.time.LocalDateTime;

public class SysRolePageVO {

    private Long id;

    private String roleName;

    private String roleKey;

    private Integer roleSort;

    private Integer dataScope;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static SysRolePageVO fromEntity(SysRole role) {
        SysRolePageVO vo = new SysRolePageVO();
        vo.setId(role.getId());
        vo.setRoleName(role.getRoleName());
        vo.setRoleKey(role.getRoleKey());
        vo.setRoleSort(role.getRoleSort());
        vo.setDataScope(role.getDataScope());
        vo.setStatus(role.getStatus());
        vo.setCreateTime(role.getCreateTime());
        vo.setRemark(role.getRemark());
        return vo;
    }

    // getter/setter 用 IDEA 自动生成

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Integer getRoleSort() {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}