package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubApiPublish;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatahubApiPublishPageVO {

    private Long id;
    private String apiCode;
    private String apiName;
    private Long datasourceId;
    private String datasourceCode;
    private String datasourceName;
    private Long tableId;
    private String tableCode;
    private String tableName;
    private String tableComment;
    private String apiPath;
    private String requestMethod;
    private String publishType;
    private Integer onlineStatus;
    private Integer authRequired;
    private String ownerName;
    private LocalDateTime publishTime;
    private LocalDateTime lastOnlineTime;
    private LocalDateTime lastOfflineTime;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubApiPublishPageVO fromEntity(DatahubApiPublish entity) {
        DatahubApiPublishPageVO vo = new DatahubApiPublishPageVO();
        vo.setId(entity.getId());
        vo.setApiCode(entity.getApiCode());
        vo.setApiName(entity.getApiName());
        vo.setDatasourceId(entity.getDatasourceId());
        vo.setDatasourceCode(entity.getDatasourceCode());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setTableId(entity.getTableId());
        vo.setTableCode(entity.getTableCode());
        vo.setTableName(entity.getTableName());
        vo.setTableComment(entity.getTableComment());
        vo.setApiPath(entity.getApiPath());
        vo.setRequestMethod(entity.getRequestMethod());
        vo.setPublishType(entity.getPublishType());
        vo.setOnlineStatus(entity.getOnlineStatus());
        vo.setAuthRequired(entity.getAuthRequired());
        vo.setOwnerName(entity.getOwnerName());
        vo.setPublishTime(entity.getPublishTime());
        vo.setLastOnlineTime(entity.getLastOnlineTime());
        vo.setLastOfflineTime(entity.getLastOfflineTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
