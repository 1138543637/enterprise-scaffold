package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubQualityResultPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String ruleName;

    private String ruleCode;

    private String tableCode;

    private String columnName;

    private Integer checkStatus;

    private Long tableId;

    private Long columnId;
}
