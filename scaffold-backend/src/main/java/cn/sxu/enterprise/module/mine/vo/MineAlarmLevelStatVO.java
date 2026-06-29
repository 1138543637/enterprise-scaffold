package cn.sxu.enterprise.module.mine.vo;

public class MineAlarmLevelStatVO {

    private Integer alarmLevel;
    private String alarmLevelName;
    private Long total;

    public MineAlarmLevelStatVO() {
    }

    public MineAlarmLevelStatVO(Integer alarmLevel, String alarmLevelName, Long total) {
        this.alarmLevel = alarmLevel;
        this.alarmLevelName = alarmLevelName;
        this.total = total;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmLevelName() {
        return alarmLevelName;
    }

    public void setAlarmLevelName(String alarmLevelName) {
        this.alarmLevelName = alarmLevelName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
