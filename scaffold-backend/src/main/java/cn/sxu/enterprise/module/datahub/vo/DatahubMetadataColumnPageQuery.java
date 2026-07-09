package cn.sxu.enterprise.module.datahub.vo;
import lombok.Data;

@Data
public class DatahubMetadataColumnPageQuery {

    public Long pageNo = 1L;

    public Long pageSize = 10L;

    public Long tableId;

    public Long dataSourceId;

    public String schemaName;

    public String tableName;

    public String columnName;

    public String dataType;

    public Integer nullableFlag;

    public Integer primaryKeyFlag;

    public Integer status;
}
