package cn.sxu.enterprise.module.datahub.vo;

import lombok.Data;

@Data
public class DatahubDashboardSummaryVO {

    private Long datasourceCount;
    private Long metadataTableCount;
    private Long metadataColumnCount;
    private Long qualityRuleCount;
    private Long qualityResultCount;
    private Long sensitiveFieldCount;
    private Long maskRuleCount;
    private Long maskResultCount;
    private Long apiPublishCount;
    private Long onlineApiCount;
}
