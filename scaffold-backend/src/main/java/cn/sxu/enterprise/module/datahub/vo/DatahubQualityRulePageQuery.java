package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubQualityRulePageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String ruleName;

    private String ruleType;

    private String targetType;

    private Long targetTableId;

    private Long targetColumnId;

    private Integer status;
}
