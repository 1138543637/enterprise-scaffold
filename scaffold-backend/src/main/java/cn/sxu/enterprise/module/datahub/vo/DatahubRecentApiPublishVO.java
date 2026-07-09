package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubApiPublish;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatahubRecentApiPublishVO {

    private Long id;
    private String apiCode;
    private String apiName;
    private String datasourceName;
    private String tableName;
    private String apiPath;
    private String requestMethod;
    private Integer onlineStatus;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;

    public static DatahubRecentApiPublishVO fromEntity(DatahubApiPublish entity) {
        DatahubRecentApiPublishVO vo = new DatahubRecentApiPublishVO();
        vo.setId(entity.getId());
        vo.setApiCode(entity.getApiCode());
        vo.setApiName(entity.getApiName());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setTableName(entity.getTableName());
        vo.setApiPath(entity.getApiPath());
        vo.setRequestMethod(entity.getRequestMethod());
        vo.setOnlineStatus(entity.getOnlineStatus());
        vo.setPublishTime(entity.getPublishTime());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
