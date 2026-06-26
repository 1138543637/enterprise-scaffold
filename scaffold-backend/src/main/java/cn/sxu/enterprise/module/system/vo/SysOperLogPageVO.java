package cn.sxu.enterprise.module.system.vo;

import cn.sxu.enterprise.module.system.entity.SysOperLog;

import java.time.LocalDateTime;

public class SysOperLogPageVO {

    private Long id;

    private String title;

    private String businessType;

    private String method;

    private String requestMethod;

    private String operatorType;

    private String operName;

    private String operUrl;

    private String operIp;

    private Integer status;

    private String errorMsg;

    private LocalDateTime operTime;

    private Long costTime;

    public static SysOperLogPageVO fromEntity(SysOperLog log) {
        SysOperLogPageVO vo = new SysOperLogPageVO();
        vo.setId(log.getId());
        vo.setTitle(log.getTitle());
        vo.setBusinessType(log.getBusinessType());
        vo.setMethod(log.getMethod());
        vo.setRequestMethod(log.getRequestMethod());
        vo.setOperatorType(log.getOperatorType());
        vo.setOperName(log.getOperName());
        vo.setOperUrl(log.getOperUrl());
        vo.setOperIp(log.getOperIp());
        vo.setStatus(log.getStatus());
        vo.setErrorMsg(log.getErrorMsg());
        vo.setOperTime(log.getOperTime());
        vo.setCostTime(log.getCostTime());
        return vo;
    }

    // 这里用 IDEA 生成 Getter / Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public LocalDateTime getOperTime() {
        return operTime;
    }

    public void setOperTime(LocalDateTime operTime) {
        this.operTime = operTime;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }
}