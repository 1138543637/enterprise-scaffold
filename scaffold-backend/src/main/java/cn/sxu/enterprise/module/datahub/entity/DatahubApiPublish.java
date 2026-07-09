package cn.sxu.enterprise.module.datahub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("datahub_api_publish")
public class DatahubApiPublish {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String apiCode;
    private String apiName;
    private Long datasourceId;
    private String datasourceCode;
    private String datasourceName;
    private Long tableId;
    private String tableCode;
    private String tableName;
    private String tableComment;
    private String apiPath;
    private String requestMethod;
    private String publishType;
    private Integer onlineStatus;
    private Integer authRequired;
    private String ownerName;
    private LocalDateTime publishTime;
    private LocalDateTime lastOnlineTime;
    private LocalDateTime lastOfflineTime;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private String remark;
}
