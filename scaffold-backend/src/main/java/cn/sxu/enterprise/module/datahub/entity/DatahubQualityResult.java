package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("datahub_quality_result")
public class DatahubQualityResult {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String resultCode;

    private Long ruleId;

    private String ruleCode;

    private String ruleName;

    private Long datasourceId;

    private String datasourceCode;

    private Long tableId;

    private String tableCode;

    private Long columnId;

    private String columnName;

    private Long checkTotal;

    private Long errorTotal;

    private BigDecimal errorRate;

    private Integer checkStatus;

    private LocalDateTime checkTime;

    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
