package cn.sxu.enterprise.module.iam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IamRecentSecurityEventVO {

    private String eventType;

    private String eventTypeName;

    private String eventCode;

    private String eventTitle;

    private String eventDetail;

    private String riskLevel;

    private String riskLevelName;

    private String statusText;

    private String operatorName;

    private String requestIp;

    private LocalDateTime eventTime;
}
