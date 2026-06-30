package cn.sxu.enterprise.module.aiops.vo;

public record AiopsAlertLevelStatVO(
        Integer alertLevel,
        String alertLevelName,
        Long total
) {
}
