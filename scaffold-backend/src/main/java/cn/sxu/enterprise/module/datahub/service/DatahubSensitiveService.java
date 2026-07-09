package cn.sxu.enterprise.module.datahub.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubMaskPreviewRequest;
import cn.sxu.enterprise.module.datahub.dto.DatahubSensitiveScanRequest;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageVO;

import java.util.List;

public interface DatahubSensitiveService {

    List<DatahubSensitiveFieldPageVO> scanSensitiveFields(DatahubSensitiveScanRequest request);

    PageResult<DatahubSensitiveFieldPageVO> pageSensitiveFields(DatahubSensitiveFieldPageQuery query);

    PageResult<DatahubMaskRulePageVO> pageMaskRules(DatahubMaskRulePageQuery query);

    DatahubMaskResultPageVO previewMask(DatahubMaskPreviewRequest request);

    PageResult<DatahubMaskResultPageVO> pageMaskResults(DatahubMaskResultPageQuery query);
}
