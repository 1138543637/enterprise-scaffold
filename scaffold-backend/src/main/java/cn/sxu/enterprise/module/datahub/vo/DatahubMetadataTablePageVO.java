package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataTable;

import java.time.LocalDateTime;

public class DatahubMetadataTablePageVO {

    public Long id;

    public Long dataSourceId;

    public String dataSourceName;

    public String schemaName;

    public String tableName;

    public String tableComment;

    public String tableType;

    public Long rowCount;

    public String collectBatchNo;

    public LocalDateTime collectTime;

    public Integer status;

    public LocalDateTime createTime;

    public String remark;

    public static DatahubMetadataTablePageVO fromEntity(DatahubMetadataTable entity) {
        DatahubMetadataTablePageVO vo = new DatahubMetadataTablePageVO();
        vo.id = entity.id;
        vo.dataSourceId = entity.dataSourceId;
        vo.dataSourceName = entity.dataSourceName;
        vo.schemaName = entity.schemaName;
        vo.tableName = entity.tableName;
        vo.tableComment = entity.tableComment;
        vo.tableType = entity.tableType;
        vo.rowCount = entity.rowCount;
        vo.collectBatchNo = entity.collectBatchNo;
        vo.collectTime = entity.collectTime;
        vo.status = entity.status;
        vo.createTime = entity.createTime;
        vo.remark = entity.remark;
        return vo;
    }
}
