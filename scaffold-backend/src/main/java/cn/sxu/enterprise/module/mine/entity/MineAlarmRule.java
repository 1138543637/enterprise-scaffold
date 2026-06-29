package cn.sxu.enterprise.module.mine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("mine_alarm_rule")
public class MineAlarmRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ruleCode;

    private String ruleName;

    private String sensorType;

    private String compareOperator;

    private BigDecimal thresholdValue;

    private Integer alarmLevel;

    private String alarmTitle;

    private String alarmContent;

    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;

    public Long getId() {
        return id;
    }

    public MineAlarmRule setId(Long id) {
        this.id = id;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmRule setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public MineAlarmRule setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmRule setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public MineAlarmRule setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public MineAlarmRule setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmRule setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public MineAlarmRule setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
        return this;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public MineAlarmRule setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmRule setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MineAlarmRule setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineAlarmRule setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public MineAlarmRule setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public MineAlarmRule setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public MineAlarmRule setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineAlarmRule setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
