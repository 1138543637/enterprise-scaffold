package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("datahub_metadata_collect_log")
public class DatahubMetadataCollectLog {

    @TableId(type = IdType.AUTO)
    public Long id;

    public String collectBatchNo;

    public Long dataSourceId;

    public String dataSourceName;

    public Integer collectStatus;

    public Integer tableTotal;

    public Integer columnTotal;

    public String errorMsg;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public Long costTime;

    public Integer status;

    public String createBy;

    public LocalDateTime createTime;

    public String updateBy;

    public LocalDateTime updateTime;

    @TableLogic
    public Integer deleted;

    public String remark;
}
