package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("datahub_mask_result")
public class DatahubMaskResult {

    @TableId(type = IdType.AUTO)
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
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
