package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatahubQualityRulePageVO {

    private Long id;

    private String ruleCode;

    private String ruleName;

    private String ruleType;

    private String targetType;

    private Long targetTableId;

    private String targetTableName;

    private Long targetColumnId;

    private String targetColumnName;

    private String checkExpression;

    private Integer qualityLevel;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
