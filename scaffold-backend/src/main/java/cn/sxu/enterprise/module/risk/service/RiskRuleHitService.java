package cn.sxu.enterprise.module.risk.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskRuleHitGenerateRequest;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageVO;

import java.util.List;

public interface RiskRuleHitService {

    List<RiskRuleHitPageVO> generate(RiskRuleHitGenerateRequest request);

    PageResult<RiskRuleHitPageVO> pageRuleHits(RiskRuleHitPageQuery query);
}
