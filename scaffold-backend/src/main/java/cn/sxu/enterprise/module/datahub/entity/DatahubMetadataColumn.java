package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("datahub_metadata_column")
public class DatahubMetadataColumn {

    @TableId(type = IdType.AUTO)
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

    public Integer sensitiveFlag;

    public String sensitiveType;

    public String maskType;

    public Integer status;

    public String createBy;

    public LocalDateTime createTime;

    public String updateBy;

    public LocalDateTime updateTime;

    @TableLogic
    public Integer deleted;

    public String remark;
}