package cn.sxu.enterprise.module.risk.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageVO;

public interface RiskRuleService {

    PageResult<RiskRulePageVO> pageRules(RiskRulePageQuery query);
}
