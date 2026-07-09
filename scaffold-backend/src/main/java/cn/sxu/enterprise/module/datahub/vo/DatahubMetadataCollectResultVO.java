package cn.sxu.enterprise.module.datahub.vo;
import lombok.Data;

@Data
public class DatahubMetadataCollectResultVO {

    public String collectBatchNo;

    public Long dataSourceId;

    public String dataSourceName;

    public Integer tableTotal;

    public Integer columnTotal;

    public Long costTime;

    public static DatahubMetadataCollectResultVO of(
            String collectBatchNo,
            Long dataSourceId,
            String dataSourceName,
            Integer tableTotal,
            Integer columnTotal,
            Long costTime
    ) {
        DatahubMetadataCollectResultVO vo = new DatahubMetadataCollectResultVO();
        vo.collectBatchNo = collectBatchNo;
        vo.dataSourceId = dataSourceId;
        vo.dataSourceName = dataSourceName;
        vo.tableTotal = tableTotal;
        vo.columnTotal = columnTotal;
        vo.costTime = costTime;
        return vo;
    }
}
