package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.module.mine.vo.MineAlarmLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentAlarmVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentWorkOrderVO;
import cn.sxu.enterprise.module.mine.vo.MineSensorTypeStatVO;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderStatusStatVO;

import java.util.List;

public interface MineDashboardService {

    MineDashboardSummaryVO getSummary();

    List<MineAlarmLevelStatVO> getAlarmLevelStats();

    List<MineSensorTypeStatVO> getSensorTypeStats();

    List<MineWorkOrderStatusStatVO> getWorkOrderStatusStats();

    List<MineRecentAlarmVO> getRecentAlarms();

    List<MineRecentWorkOrderVO> getRecentWorkOrders();
}
