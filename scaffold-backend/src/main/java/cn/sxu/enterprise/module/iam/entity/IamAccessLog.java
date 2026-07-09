package cn.sxu.enterprise.module.iam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * IAM 接口访问日志实体。
 */
@Data
@TableName("iam_access_log")
public class IamAccessLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String traceId;

    private String requestUri;

    private String requestMethod;

    private String moduleName;

    private String operationName;

    private Long userId;

    private String username;

    private String clientIp;

    private String userAgent;

    private String requestParams;

    private Integer responseCode;

    private String responseMsg;

    /**
     * 访问状态：0成功，1失败。
     */
    private Integer accessStatus;

    private Long costMs;

    private LocalDateTime accessTime;

    /**
     * 业务状态：0有效，1无效。
     */
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
