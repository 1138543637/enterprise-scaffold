package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubMaskResultPageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;

    private String tableName;
    private String columnName;
    private String sensitiveType;
    private String maskMethod;
    private Integer maskStatus;
    private Integer status;
}
