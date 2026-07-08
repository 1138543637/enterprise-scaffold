package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DatahubQualityResultPageVO {

    private Long id;

    private String resultCode;

    private Long ruleId;

    private String ruleCode;

    private String ruleName;

    private Long datasourceId;

    private String datasourceCode;

    private Long tableId;

    private String tableCode;

    private Long columnId;

    private String columnName;

    private Long checkTotal;

    private Long errorTotal;

    private BigDecimal errorRate;

    private Integer checkStatus;

    private LocalDateTime checkTime;

    private Integer status;

    private String remark;
}
