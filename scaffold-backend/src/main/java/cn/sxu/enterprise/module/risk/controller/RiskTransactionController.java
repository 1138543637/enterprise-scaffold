package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.dto.RiskTransactionSimulateRequest;
import cn.sxu.enterprise.module.risk.service.RiskTransactionService;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk/transactions")
public class RiskTransactionController {

    private final RiskTransactionService riskTransactionService;

    public RiskTransactionController(RiskTransactionService riskTransactionService) {
        this.riskTransactionService = riskTransactionService;
    }

    @OperLog(title = "银行风控-交易流水", businessType = "交易模拟")
    @PostMapping("/simulate")
    public ApiResult<List<RiskTransactionVO>> simulate(@Valid @RequestBody RiskTransactionSimulateRequest request) {
        return ApiResult.success(riskTransactionService.simulateTransactions(request));
    }

    @OperLog(title = "银行风控-交易流水", businessType = "最新交易查询")
    @GetMapping("/latest")
    public ApiResult<List<RiskTransactionVO>> latest() {
        return ApiResult.success(riskTransactionService.latestTransactions());
    }

    @OperLog(title = "银行风控-交易流水", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<RiskTransactionPageVO>> page(RiskTransactionPageQuery query) {
        return ApiResult.success(riskTransactionService.pageTransactions(query));
    }
}
