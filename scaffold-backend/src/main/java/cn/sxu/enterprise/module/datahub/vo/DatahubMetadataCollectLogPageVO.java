package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataCollectLog;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DatahubMetadataCollectLogPageVO {

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

    public LocalDateTime createTime;

    public String remark;

    public static DatahubMetadataCollectLogPageVO fromEntity(DatahubMetadataCollectLog entity) {
        DatahubMetadataCollectLogPageVO vo = new DatahubMetadataCollectLogPageVO();
        vo.id = entity.id;
        vo.collectBatchNo = entity.collectBatchNo;
        vo.dataSourceId = entity.dataSourceId;
        vo.dataSourceName = entity.dataSourceName;
        vo.collectStatus = entity.collectStatus;
        vo.tableTotal = entity.tableTotal;
        vo.columnTotal = entity.columnTotal;
        vo.errorMsg = entity.errorMsg;
        vo.startTime = entity.startTime;
        vo.endTime = entity.endTime;
        vo.costTime = entity.costTime;
        vo.status = entity.status;
        vo.createTime = entity.createTime;
        vo.remark = entity.remark;
        return vo;
    }
}
