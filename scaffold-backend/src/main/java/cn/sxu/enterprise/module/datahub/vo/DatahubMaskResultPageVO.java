package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubMaskResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatahubMaskResultPageVO {

    private Long id;
    private String resultCode;
    private Long fieldId;
    private Long ruleId;
    private Long datasourceId;
    private String datasourceName;
    private Long tableId;
    private String tableName;
    private Long columnId;
    private String columnName;
    private String sensitiveType;
    private String maskMethod;
    private String sampleBefore;
    private String sampleAfter;
    private Integer maskStatus;
    private LocalDateTime maskTime;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubMaskResultPageVO fromEntity(DatahubMaskResult entity) {
        if (entity == null) {
            return null;
        }
        DatahubMaskResultPageVO vo = new DatahubMaskResultPageVO();
        vo.setId(entity.getId());
        vo.setResultCode(entity.getResultCode());
        vo.setFieldId(entity.getFieldId());
        vo.setRuleId(entity.getRuleId());
        vo.setDatasourceId(entity.getDatasourceId());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setTableId(entity.getTableId());
        vo.setTableName(entity.getTableName());
        vo.setColumnId(entity.getColumnId());
        vo.setColumnName(entity.getColumnName());
        vo.setSensitiveType(entity.getSensitiveType());
        vo.setMaskMethod(entity.getMaskMethod());
        vo.setSampleBefore(entity.getSampleBefore());
        vo.setSampleAfter(entity.getSampleAfter());
        vo.setMaskStatus(entity.getMaskStatus());
        vo.setMaskTime(entity.getMaskTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
