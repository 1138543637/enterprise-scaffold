package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.service.RiskRuleService;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRulePageVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk/rules")
public class RiskRuleController {

    @Resource
    private RiskRuleService riskRuleService;

    @GetMapping("/page")
    @OperLog(title = "银行风控-规则管理", businessType = "分页查询")
    public ApiResult<PageResult<RiskRulePageVO>> pageRules(RiskRulePageQuery query) {
        return ApiResult.success(riskRuleService.pageRules(query));
    }
}
