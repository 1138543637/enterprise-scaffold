package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataColumn;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DatahubMetadataColumnPageVO {

    public Long id;

    public Long tableId;

    public Long dataSourceId;

    public String dataSourceName;

    public String schemaName;

    public String tableName;

    public String columnName;

    public String columnComment;

    public String columnType;

    public String dataType;

    public Integer columnLength;

    public Integer numericPrecision;

    public Integer numericScale;

    public Integer nullableFlag;

    public Integer primaryKeyFlag;

    public String columnDefault;

    public Integer ordinalPosition;

    public String collectBatchNo;

    public LocalDateTime collectTime;

    public Integer status;

    public LocalDateTime createTime;

    public String remark;

    public static DatahubMetadataColumnPageVO fromEntity(DatahubMetadataColumn entity) {
        DatahubMetadataColumnPageVO vo = new DatahubMetadataColumnPageVO();
        vo.id = entity.id;
        vo.tableId = entity.tableId;
        vo.dataSourceId = entity.dataSourceId;
        vo.dataSourceName = entity.dataSourceName;
        vo.schemaName = entity.schemaName;
        vo.tableName = entity.tableName;
        vo.columnName = entity.columnName;
        vo.columnComment = entity.columnComment;
        vo.columnType = entity.columnType;
        vo.dataType = entity.dataType;
        vo.columnLength = entity.columnLength;
        vo.numericPrecision = entity.numericPrecision;
        vo.numericScale = entity.numericScale;
        vo.nullableFlag = entity.nullableFlag;
        vo.primaryKeyFlag = entity.primaryKeyFlag;
        vo.columnDefault = entity.columnDefault;
        vo.ordinalPosition = entity.ordinalPosition;
        vo.collectBatchNo = entity.collectBatchNo;
        vo.collectTime = entity.collectTime;
        vo.status = entity.status;
        vo.createTime = entity.createTime;
        vo.remark = entity.remark;
        return vo;
    }
}
