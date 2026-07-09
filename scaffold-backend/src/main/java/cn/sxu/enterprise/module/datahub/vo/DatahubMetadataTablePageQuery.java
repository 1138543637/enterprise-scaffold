package cn.sxu.enterprise.module.datahub.vo;
import lombok.Data;

@Data
public class DatahubMetadataTablePageQuery {

    public Long pageNo = 1L;

    public Long pageSize = 10L;

    public Long dataSourceId;

    public String schemaName;

    public String tableName;

    public String tableType;

    public Integer status;
}
