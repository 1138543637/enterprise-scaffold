package cn.sxu.enterprise.module.datahub.vo;

import cn.sxu.enterprise.module.datahub.entity.DatahubQualityResult;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DatahubRecentQualityResultVO {

    private Long id;
    private String resultCode;
    private String ruleCode;
    private String ruleName;
    private String datasourceCode;
    private String tableCode;
    private String columnName;
    private Long checkTotal;
    private Long errorTotal;
    private BigDecimal errorRate;
    private Integer checkStatus;
    private LocalDateTime checkTime;
    private Integer status;
    private LocalDateTime createTime;
    private String remark;

    public static DatahubRecentQualityResultVO fromEntity(DatahubQualityResult entity) {
        DatahubRecentQualityResultVO vo = new DatahubRecentQualityResultVO();
        vo.setId(entity.getId());
        vo.setResultCode(entity.getResultCode());
        vo.setRuleCode(entity.getRuleCode());
        vo.setRuleName(entity.getRuleName());
        vo.setDatasourceCode(entity.getDatasourceCode());
        vo.setTableCode(entity.getTableCode());
        vo.setColumnName(entity.getColumnName());
        vo.setCheckTotal(entity.getCheckTotal());
        vo.setErrorTotal(entity.getErrorTotal());
        vo.setErrorRate(entity.getErrorRate());
        vo.setCheckStatus(entity.getCheckStatus());
        vo.setCheckTime(entity.getCheckTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
