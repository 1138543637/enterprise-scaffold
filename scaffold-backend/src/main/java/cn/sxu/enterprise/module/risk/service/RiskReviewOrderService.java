package cn.sxu.enterprise.module.risk.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.risk.dto.RiskReviewApproveRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewOrderCreateRequest;
import cn.sxu.enterprise.module.risk.dto.RiskReviewRejectRequest;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageQuery;
import cn.sxu.enterprise.module.risk.vo.RiskReviewOrderPageVO;
import cn.sxu.enterprise.module.risk.vo.RiskReviewSummaryVO;

import java.util.List;

public interface RiskReviewOrderService {

    PageResult<RiskReviewOrderPageVO> pageReviewOrders(RiskReviewOrderPageQuery query);

    RiskReviewSummaryVO summary();

    List<RiskReviewOrderPageVO> createFromTransaction(RiskReviewOrderCreateRequest request);

    RiskReviewOrderPageVO approve(Long id, RiskReviewApproveRequest request);

    RiskReviewOrderPageVO reject(Long id, RiskReviewRejectRequest request);
}
