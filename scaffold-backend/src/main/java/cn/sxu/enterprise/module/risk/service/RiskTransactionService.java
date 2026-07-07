package cn.sxu.enterprise.module.risk.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskTransactionSimulateRequest;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionVO;

import java.util.List;

public interface RiskTransactionService {

    List<RiskTransactionVO> simulateTransactions(RiskTransactionSimulateRequest request);

    List<RiskTransactionVO> latestTransactions();

    PageResult<RiskTransactionPageVO> pageTransactions(RiskTransactionPageQuery query);
}
