package cn.sxu.enterprise.module.datahub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DatahubSensitiveScanRequest {

    /**
     * 可选：只扫描某一张元数据表。
     */
    private Long targetTableId;

    /**
     * 可选：只保留某一类识别结果，例如 PHONE、EMAIL、ID_CARD。
     */
    private String sensitiveType;

    /**
     * 最多扫描字段数，默认 500，最大 5000。
     */
    @Min(value = 1, message = "扫描字段数至少为1")
    @Max(value = 5000, message = "扫描字段数不能超过5000")
    private Integer limit = 500;
}
