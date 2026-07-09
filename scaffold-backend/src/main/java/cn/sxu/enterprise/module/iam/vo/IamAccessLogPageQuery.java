package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * IAM 接口访问日志分页查询条件。
 */
@Data
public class IamAccessLogPageQuery {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String requestUri;

    private String requestMethod;

    private String username;

    private String clientIp;

    /**
     * 访问状态：0成功，1失败。
     */
    private Integer accessStatus;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
