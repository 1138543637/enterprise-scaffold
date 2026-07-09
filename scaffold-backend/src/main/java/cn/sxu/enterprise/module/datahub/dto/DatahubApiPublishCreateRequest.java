package cn.sxu.enterprise.module.datahub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DatahubApiPublishCreateRequest {

    @NotNull(message = "元数据表ID不能为空")
    private Long tableId;

    private String apiName;
    private String apiPath;
    private String requestMethod;
    private String ownerName;
    private String remark;
}
