package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysLoginLog;

import java.time.LocalDateTime;

public class SysLoginLogPageVO {

    private Long id;

    private String username;

    private String ipaddr;

    private String loginLocation;

    private String browser;

    private String os;

    private Integer status;

    private String msg;

    private LocalDateTime loginTime;

    public static SysLoginLogPageVO fromEntity(SysLoginLog log) {
        SysLoginLogPageVO vo = new SysLoginLogPageVO();
        vo.setId(log.getId());
        vo.setUsername(log.getUsername());
        vo.setIpaddr(log.getIpaddr());
        vo.setLoginLocation(log.getLoginLocation());
        vo.setBrowser(log.getBrowser());
        vo.setOs(log.getOs());
        vo.setStatus(log.getStatus());
        vo.setMsg(log.getMsg());
        vo.setLoginTime(log.getLoginTime());
        return vo;
    }

    // 这里用 IDEA 生成 Getter / Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}