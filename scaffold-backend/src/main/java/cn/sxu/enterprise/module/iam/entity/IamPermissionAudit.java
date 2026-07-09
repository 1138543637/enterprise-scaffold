package cn.sxu.enterprise.module.iam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("iam_permission_audit")
public class IamPermissionAudit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String auditCode;

    private String auditType;

    private String targetType;

    private Long targetId;

    private String targetName;

    private String changeAction;

    private String beforeValue;

    private String afterValue;

    private String riskLevel;

    private String reviewStatus;

    private String reviewBy;

    private LocalDateTime reviewTime;

    private String requestIp;

    private String operatorName;

    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
