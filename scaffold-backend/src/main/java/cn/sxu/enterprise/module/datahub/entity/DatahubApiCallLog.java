package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("datahub_api_call_log")
public class DatahubApiCallLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String callCode;
    private Long apiId;
    private String apiCode;
    private String apiName;
    private String requestPath;
    private String requestMethod;
    private String callerIp;
    private Integer callStatus;
    private String errorMsg;
    private Long costTime;
    private LocalDateTime callTime;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
