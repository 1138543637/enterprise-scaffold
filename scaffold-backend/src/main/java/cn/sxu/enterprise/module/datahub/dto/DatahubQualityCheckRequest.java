package cn.sxu.enterprise.module.datahub.dto;

import lombok.Data;

@Data
public class DatahubQualityCheckRequest {

    /**
     * 规则ID。传入 ruleId 时只检测单条规则。
     */
    private Long ruleId;

    /**
     * 元数据表ID。不传 ruleId、只传 targetTableId 时，检测该表下全部启用规则。
     */
    private Long targetTableId;
}
