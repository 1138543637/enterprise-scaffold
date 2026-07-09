package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("datahub_mask_rule")
public class DatahubMaskRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ruleCode;
    private String ruleName;
    private String sensitiveType;
    private String maskMethod;
    private Integer keepPrefix;
    private Integer keepSuffix;
    private String maskChar;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
