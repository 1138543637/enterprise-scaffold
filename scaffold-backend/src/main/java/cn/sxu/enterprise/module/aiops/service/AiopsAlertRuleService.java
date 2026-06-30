package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsAlertRulePageVO;

public interface AiopsAlertRuleService {
    PageResult<AiopsAlertRulePageVO> pageAlertRules(AiopsAlertRulePageQuery query);
}
