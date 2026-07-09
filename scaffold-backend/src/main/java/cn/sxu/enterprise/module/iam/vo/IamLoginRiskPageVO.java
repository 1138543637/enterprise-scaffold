package cn.sxu.enterprise.module.iam.vo;

import cn.sxu.enterprise.module.iam.entity.IamLoginRisk;

import java.time.LocalDateTime;

/**
 * IAM 异常登录风险分页展示 VO。
 */
public class IamLoginRiskPageVO {

    private Long id;

    private String riskCode;

    private String username;

    private String clientIp;

    private String riskType;

    private String riskTypeName;

    private Integer riskLevel;

    private String riskLevelName;

    private Integer failCount;

    private LocalDateTime firstFailTime;

    private LocalDateTime lastFailTime;

    private LocalDateTime detectTime;

    private Integer handleStatus;

    private String handleStatusName;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static IamLoginRiskPageVO fromEntity(IamLoginRisk entity) {
        IamLoginRiskPageVO vo = new IamLoginRiskPageVO();
        if (entity == null) {
            return vo;
        }
        vo.setId(entity.getId());
        vo.setRiskCode(entity.getRiskCode());
        vo.setUsername(entity.getUsername());
        vo.setClientIp(entity.getClientIp());
        vo.setRiskType(entity.getRiskType());
        vo.setRiskTypeName(resolveRiskTypeName(entity.getRiskType()));
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setRiskLevelName(resolveRiskLevelName(entity.getRiskLevel()));
        vo.setFailCount(entity.getFailCount());
        vo.setFirstFailTime(entity.getFirstFailTime());
        vo.setLastFailTime(entity.getLastFailTime());
        vo.setDetectTime(entity.getDetectTime());
        vo.setHandleStatus(entity.getHandleStatus());
        vo.setHandleStatusName(resolveHandleStatusName(entity.getHandleStatus()));
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String resolveRiskTypeName(String riskType) {
        if ("LOGIN_FAILED".equals(riskType)) {
            return "登录失败";
        }
        if ("SHORT_TIME_FAILED".equals(riskType)) {
            return "短时间多次失败";
        }
        if ("ABNORMAL_IP".equals(riskType)) {
            return "异常 IP";
        }
        return "未知风险";
    }

    private static String resolveRiskLevelName(Integer riskLevel) {
        if (riskLevel == null) {
            return "未知";
        }
        if (riskLevel == 1) {
            return "低风险";
        }
        if (riskLevel == 2) {
            return "中风险";
        }
        if (riskLevel == 3) {
            return "高风险";
        }
        return "未知";
    }

    private static String resolveHandleStatusName(Integer handleStatus) {
        if (handleStatus == null) {
            return "未知";
        }
        if (handleStatus == 0) {
            return "未处理";
        }
        if (handleStatus == 1) {
            return "已确认";
        }
        if (handleStatus == 2) {
            return "已忽略";
        }
        return "未知";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getRiskTypeName() {
        return riskTypeName;
    }

    public void setRiskTypeName(String riskTypeName) {
        this.riskTypeName = riskTypeName;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevelName() {
        return riskLevelName;
    }

    public void setRiskLevelName(String riskLevelName) {
        this.riskLevelName = riskLevelName;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public LocalDateTime getFirstFailTime() {
        return firstFailTime;
    }

    public void setFirstFailTime(LocalDateTime firstFailTime) {
        this.firstFailTime = firstFailTime;
    }

    public LocalDateTime getLastFailTime() {
        return lastFailTime;
    }

    public void setLastFailTime(LocalDateTime lastFailTime) {
        this.lastFailTime = lastFailTime;
    }

    public LocalDateTime getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(LocalDateTime detectTime) {
        this.detectTime = detectTime;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleStatusName() {
        return handleStatusName;
    }

    public void setHandleStatusName(String handleStatusName) {
        this.handleStatusName = handleStatusName;
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
