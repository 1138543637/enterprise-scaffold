package cn.sxu.enterprise.module.datahub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DatahubMaskPreviewRequest {

    @NotNull(message = "敏感字段ID不能为空")
    private Long fieldId;

    /**
     * 可选：指定脱敏规则。不传时，后端按 sensitiveType 自动选启用规则。
     */
    private Long maskRuleId;

    /**
     * 可选：前端传入示例值。不传时，后端根据敏感类型生成一个演示值。
     */
    private String rawValue;
}
