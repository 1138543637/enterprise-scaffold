package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubMaskRulePageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;

    private String ruleCode;
    private String ruleName;
    private String sensitiveType;
    private String maskMethod;
    private Integer status;
}
