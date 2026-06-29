package cn.sxu.enterprise.module.mine.vo;

public record MineMaintenanceTaskStatusStatVO(
        Integer taskStatus,
        String taskStatusName,
        Long total
) {
}
