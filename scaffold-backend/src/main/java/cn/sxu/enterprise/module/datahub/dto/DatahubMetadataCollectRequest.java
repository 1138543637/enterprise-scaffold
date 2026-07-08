package cn.sxu.enterprise.module.datahub.dto;

import jakarta.validation.constraints.NotNull;

public class DatahubMetadataCollectRequest {

    @NotNull(message = "数据源ID不能为空")
    public Long dataSourceId;
}
