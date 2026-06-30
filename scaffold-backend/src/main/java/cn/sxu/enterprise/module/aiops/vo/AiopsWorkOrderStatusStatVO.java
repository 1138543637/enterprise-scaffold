package cn.sxu.enterprise.module.aiops.vo;

public record AiopsWorkOrderStatusStatVO(
        Integer orderStatus,
        String orderStatusName,
        Long total
) {
}
