package cn.sxu.enterprise.module.iam.vo;

import cn.sxu.enterprise.module.iam.entity.IamAccessLog;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * IAM 接口访问日志分页展示对象。
 */
@Data
public class IamAccessLogPageVO {

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

    private Integer responseCode;

    private String responseMsg;

    private Integer accessStatus;

    private String accessStatusName;

    private Long costMs;

    private LocalDateTime accessTime;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    public static IamAccessLogPageVO fromEntity(IamAccessLog entity) {
        IamAccessLogPageVO vo = new IamAccessLogPageVO();
        vo.setId(entity.getId());
        vo.setTraceId(entity.getTraceId());
        vo.setRequestUri(entity.getRequestUri());
        vo.setRequestMethod(entity.getRequestMethod());
        vo.setModuleName(entity.getModuleName());
        vo.setOperationName(entity.getOperationName());
        vo.setUserId(entity.getUserId());
        vo.setUsername(entity.getUsername());
        vo.setClientIp(entity.getClientIp());
        vo.setUserAgent(entity.getUserAgent());
        vo.setResponseCode(entity.getResponseCode());
        vo.setResponseMsg(entity.getResponseMsg());
        vo.setAccessStatus(entity.getAccessStatus());
        vo.setAccessStatusName(getAccessStatusName(entity.getAccessStatus()));
        vo.setCostMs(entity.getCostMs());
        vo.setAccessTime(entity.getAccessTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    private static String getAccessStatusName(Integer accessStatus) {
        if (accessStatus == null) {
            return "未知";
        }
        if (accessStatus == 0) {
            return "成功";
        }
        if (accessStatus == 1) {
            return "失败";
        }
        return "未知";
    }
}
