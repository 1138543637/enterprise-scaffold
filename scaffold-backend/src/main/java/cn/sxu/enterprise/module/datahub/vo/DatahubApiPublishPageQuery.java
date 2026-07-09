package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubApiPublishPageQuery {

    private Long pageNo = 1L;
    private Long pageSize = 10L;
    private String apiCode;
    private String apiName;
    private String datasourceName;
    private String tableName;
    private String requestMethod;
    private Integer onlineStatus;
    private Integer status;
}
