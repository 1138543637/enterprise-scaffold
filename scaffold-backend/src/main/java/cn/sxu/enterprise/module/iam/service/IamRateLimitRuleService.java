package cn.sxu.enterprise.module.iam.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageQuery;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRulePageVO;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateRequest;
import cn.sxu.enterprise.module.iam.vo.IamRateLimitRuleSimulateVO;

public interface IamRateLimitRuleService {

    PageResult<IamRateLimitRulePageVO> pageRateLimitRules(IamRateLimitRulePageQuery query);

    Boolean enableRule(Long id);

    Boolean disableRule(Long id);

    IamRateLimitRuleSimulateVO simulate(IamRateLimitRuleSimulateRequest request);
}
