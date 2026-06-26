package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysUser;

import java.time.LocalDateTime;

public class SysUserPageVO {

    private Long id;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private Integer gender;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static SysUserPageVO fromEntity(SysUser user) {
        SysUserPageVO vo = new SysUserPageVO();
        vo.setId(user.getId());
        vo.setDeptId(user.getDeptId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setGender(user.getGender());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        vo.setRemark(user.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getGender() {
        return gender;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}