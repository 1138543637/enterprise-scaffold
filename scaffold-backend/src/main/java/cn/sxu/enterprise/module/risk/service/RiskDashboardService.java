package cn.sxu.enterprise.module.risk.service;

import cn.sxu.enterprise.module.risk.vo.RiskChannelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskDashboardSummaryVO;
import cn.sxu.enterprise.module.risk.vo.RiskLevelStatVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentReviewOrderVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentRuleHitVO;
import cn.sxu.enterprise.module.risk.vo.RiskRecentTransactionVO;
import cn.sxu.enterprise.module.risk.vo.RiskTransactionTypeStatVO;

import java.util.List;

public interface RiskDashboardService {

    RiskDashboardSummaryVO getSummary();

    List<RiskChannelStatVO> getChannelStats();

    List<RiskTransactionTypeStatVO> getTransactionTypeStats();

    List<RiskLevelStatVO> getRiskLevelStats();

    List<RiskRecentTransactionVO> getRecentTransactions();

    List<RiskRecentRuleHitVO> getRecentRuleHits();

    List<RiskRecentReviewOrderVO> getRecentReviewOrders();
}
