package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineMaintenanceTask;

import java.time.LocalDateTime;

public class MineMaintenanceTaskPageVO {

    private Long id;

    private String taskCode;

    private Long deviceId;

    private String deviceCode;

    private String deviceName;

    private String deviceType;

    private String areaName;

    private String location;

    private Integer healthScore;

    private Integer riskLevel;

    private String riskLevelName;

    private String taskTitle;

    private String taskContent;

    private Integer taskType;

    private Integer taskSource;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer priority;

    private String priorityName;

    private LocalDateTime planStartTime;

    private LocalDateTime planEndTime;

    private Long maintainerUserId;

    private String maintainerUsername;

    private LocalDateTime handleTime;

    private String handleResult;

    private LocalDateTime closeTime;

    private String closeResult;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static MineMaintenanceTaskPageVO fromEntity(MineMaintenanceTask entity) {
        MineMaintenanceTaskPageVO vo = new MineMaintenanceTaskPageVO();
        vo.setId(entity.getId());
        vo.setTaskCode(entity.getTaskCode());
        vo.setDeviceId(entity.getDeviceId());
        vo.setDeviceCode(entity.getDeviceCode());
        vo.setDeviceName(entity.getDeviceName());
        vo.setDeviceType(entity.getDeviceType());
        vo.setAreaName(entity.getAreaName());
        vo.setLocation(entity.getLocation());
        vo.setHealthScore(entity.getHealthScore());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setRiskLevelName(entity.getRiskLevelName());
        vo.setTaskTitle(entity.getTaskTitle());
        vo.setTaskContent(entity.getTaskContent());
        vo.setTaskType(entity.getTaskType());
        vo.setTaskSource(entity.getTaskSource());
        vo.setTaskStatus(entity.getTaskStatus());
        vo.setTaskStatusName(toTaskStatusName(entity.getTaskStatus()));
        vo.setPriority(entity.getPriority());
        vo.setPriorityName(toPriorityName(entity.getPriority()));
        vo.setPlanStartTime(entity.getPlanStartTime());
        vo.setPlanEndTime(entity.getPlanEndTime());
        vo.setMaintainerUserId(entity.getMaintainerUserId());
        vo.setMaintainerUsername(entity.getMaintainerUsername());
        vo.setHandleTime(entity.getHandleTime());
        vo.setHandleResult(entity.getHandleResult());
        vo.setCloseTime(entity.getCloseTime());
        vo.setCloseResult(entity.getCloseResult());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String toTaskStatusName(Integer taskStatus) {
        if (taskStatus == null) {
            return "未知";
        }
        return switch (taskStatus) {
            case 0 -> "待安排";
            case 1 -> "待执行";
            case 2 -> "处理中";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    private static String toPriorityName(Integer priority) {
        if (priority == null) {
            return "未知";
        }
        return switch (priority) {
            case 0 -> "低";
            case 1 -> "中";
            case 2 -> "高";
            case 3 -> "紧急";
            default -> "未知";
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
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

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(Integer taskSource) {
        this.taskSource = taskSource;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public LocalDateTime getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(LocalDateTime planStartTime) {
        this.planStartTime = planStartTime;
    }

    public LocalDateTime getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(LocalDateTime planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Long getMaintainerUserId() {
        return maintainerUserId;
    }

    public void setMaintainerUserId(Long maintainerUserId) {
        this.maintainerUserId = maintainerUserId;
    }

    public String getMaintainerUsername() {
        return maintainerUsername;
    }

    public void setMaintainerUsername(String maintainerUsername) {
        this.maintainerUsername = maintainerUsername;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseResult() {
        return closeResult;
    }

    public void setCloseResult(String closeResult) {
        this.closeResult = closeResult;
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
