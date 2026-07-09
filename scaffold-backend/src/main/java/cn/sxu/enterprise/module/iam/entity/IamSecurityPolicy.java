package cn.sxu.enterprise.module.iam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("iam_security_policy")
public class IamSecurityPolicy {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String policyCode;

    private String policyName;

    private String policyType;

    private Integer policyLevel;

    private String policyValue;

    private String policyUnit;

    private String effectiveScope;

    private Integer enabled;

    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
