package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.dto.RiskRuleHitGenerateRequest;
import cn.sxu.enterprise.module.risk.service.RiskRuleHitService;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskRuleHitPageVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk/rule-hits")
public class RiskRuleHitController {

    @Resource
    private RiskRuleHitService riskRuleHitService;

    @PostMapping("/generate")
    @OperLog(title = "银行风控-规则命中", businessType = "生成规则命中")
    public ApiResult<List<RiskRuleHitPageVO>> generate(@RequestBody(required = false) RiskRuleHitGenerateRequest request) {
        RiskRuleHitGenerateRequest safeRequest = request == null ? new RiskRuleHitGenerateRequest() : request;
        return ApiResult.success(riskRuleHitService.generate(safeRequest));
    }

    @GetMapping("/page")
    @OperLog(title = "银行风控-规则命中", businessType = "分页查询")
    public ApiResult<PageResult<RiskRuleHitPageVO>> pageRuleHits(RiskRuleHitPageQuery query) {
        return ApiResult.success(riskRuleHitService.pageRuleHits(query));
    }
}
