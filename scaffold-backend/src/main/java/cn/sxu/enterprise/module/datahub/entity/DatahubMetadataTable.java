package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("datahub_metadata_table")
public class DatahubMetadataTable {

    @TableId(type = IdType.AUTO)
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

    public String createBy;

    public LocalDateTime createTime;

    public String updateBy;

    public LocalDateTime updateTime;

    @TableLogic
    public Integer deleted;

    public String remark;
}
