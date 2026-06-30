package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsAlertGenerateRequest;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertEventPageVO;
import java.util.List;

public interface AiopsAlertEventService {
    PageResult<AiopsAlertEventPageVO> pageAlertEvents(AiopsAlertEventPageQuery query);
    List<AiopsAlertEventPageVO> generate(AiopsAlertGenerateRequest request);
}
