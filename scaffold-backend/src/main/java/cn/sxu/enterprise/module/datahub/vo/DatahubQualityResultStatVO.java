package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubQualityResultStatVO {

    private Integer checkStatus;
    private String statusName;
    private Long count;
}
