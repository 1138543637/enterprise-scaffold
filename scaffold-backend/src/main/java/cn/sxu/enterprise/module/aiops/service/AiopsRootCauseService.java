package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsRootCauseAnalyzeRequest;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCausePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCauseVO;

public interface AiopsRootCauseService {

    AiopsRootCauseVO analyze(AiopsRootCauseAnalyzeRequest request);

    PageResult<AiopsRootCauseVO> pageRootCauses(AiopsRootCausePageQuery query);

    AiopsRootCauseVO detail(Long id);
}
