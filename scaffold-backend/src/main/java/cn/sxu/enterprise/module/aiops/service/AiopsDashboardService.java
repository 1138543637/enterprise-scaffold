package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.module.aiops.vo.AiopsAlertLevelStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsDashboardSummaryVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricTrendVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentAlertVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentRootCauseVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsRecentWorkOrderVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourceTypeStatVO;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderStatusStatVO;

import java.util.List;

public interface AiopsDashboardService {

    AiopsDashboardSummaryVO getSummary();

    List<AiopsResourceTypeStatVO> getResourceTypeStats();

    List<AiopsAlertLevelStatVO> getAlertLevelStats();

    List<AiopsWorkOrderStatusStatVO> getWorkOrderStatusStats();

    List<AiopsMetricTrendVO> getMetricTrend();

    List<AiopsRecentAlertVO> getRecentAlerts();

    List<AiopsRecentWorkOrderVO> getRecentWorkOrders();

    List<AiopsRecentRootCauseVO> getRecentRootCauses();
}
