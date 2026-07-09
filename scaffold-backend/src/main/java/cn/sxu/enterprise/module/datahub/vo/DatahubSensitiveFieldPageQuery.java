package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubSensitiveFieldPageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;

    private String datasourceName;
    private String tableName;
    private String columnName;
    private String sensitiveType;
    private Integer confirmStatus;
    private Integer status;
}
