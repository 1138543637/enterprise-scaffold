package cn.sxu.enterprise.module.mine.vo;

import cn.sxu.enterprise.module.mine.entity.MineAlarmRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MineAlarmRulePageVO {

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

    private LocalDateTime createTime;

    private String remark;

    public static MineAlarmRulePageVO fromEntity(MineAlarmRule entity) {
        if (entity == null) {
            return null;
        }

        MineAlarmRulePageVO vo = new MineAlarmRulePageVO();
        vo.setId(entity.getId());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setSensorType(entity.getSensorType());
        vo.setCompareOperator(entity.getCompareOperator());
        vo.setThresholdValue(entity.getThresholdValue());
        vo.setAlarmLevel(entity.getAlarmLevel());
        vo.setAlarmTitle(entity.getAlarmTitle());
        vo.setAlarmContent(entity.getAlarmContent());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public MineAlarmRulePageVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public MineAlarmRulePageVO setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public MineAlarmRulePageVO setRuleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public String getSensorType() {
        return sensorType;
    }

    public MineAlarmRulePageVO setSensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public MineAlarmRulePageVO setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
        return this;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public MineAlarmRulePageVO setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
        return this;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public MineAlarmRulePageVO setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
        return this;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public MineAlarmRulePageVO setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
        return this;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public MineAlarmRulePageVO setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MineAlarmRulePageVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public MineAlarmRulePageVO setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MineAlarmRulePageVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
