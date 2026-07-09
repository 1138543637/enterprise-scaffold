package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("datahub_sensitive_field")
public class DatahubSensitiveField {

    @TableId(type = IdType.AUTO)
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
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
