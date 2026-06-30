package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageVO;

public interface AiopsResourceService {

    PageResult<AiopsResourcePageVO> pageResources(AiopsResourcePageQuery query);
}
