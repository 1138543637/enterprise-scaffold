package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubQualityCheckRequest;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageVO;

import java.util.List;

public interface DatahubQualityService {

    PageResult<DatahubQualityRulePageVO> pageRules(DatahubQualityRulePageQuery query);

    List<DatahubQualityResultPageVO> check(DatahubQualityCheckRequest request);

    PageResult<DatahubQualityResultPageVO> pageResults(DatahubQualityResultPageQuery query);
}
