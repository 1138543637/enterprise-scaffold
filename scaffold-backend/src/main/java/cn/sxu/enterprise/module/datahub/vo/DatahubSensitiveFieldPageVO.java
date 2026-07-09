package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubSensitiveField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DatahubSensitiveFieldPageVO {

    private Long id;
    private String fieldCode;
    private Long datasourceId;
    private String datasourceName;
    private Long tableId;
    private String schemaName;
    private String tableName;
    private Long columnId;
    private String columnName;
    private String columnComment;
    private String dataType;
    private String sensitiveType;
    private Integer sensitiveLevel;
    private String detectRule;
    private BigDecimal confidence;
    private Integer confirmStatus;
    private String maskType;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubSensitiveFieldPageVO fromEntity(DatahubSensitiveField entity) {
        if (entity == null) {
            return null;
        }
        DatahubSensitiveFieldPageVO vo = new DatahubSensitiveFieldPageVO();
        vo.setId(entity.getId());
        vo.setFieldCode(entity.getFieldCode());
        vo.setDatasourceId(entity.getDatasourceId());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setTableId(entity.getTableId());
        vo.setSchemaName(entity.getSchemaName());
        vo.setTableName(entity.getTableName());
        vo.setColumnId(entity.getColumnId());
        vo.setColumnName(entity.getColumnName());
        vo.setColumnComment(entity.getColumnComment());
        vo.setDataType(entity.getDataType());
        vo.setSensitiveType(entity.getSensitiveType());
        vo.setSensitiveLevel(entity.getSensitiveLevel());
        vo.setDetectRule(entity.getDetectRule());
        vo.setConfidence(entity.getConfidence());
        vo.setConfirmStatus(entity.getConfirmStatus());
        vo.setMaskType(entity.getMaskType());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
