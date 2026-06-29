package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.module.mine.vo.MineMaintenanceDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceHighRiskDeviceVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenancePriorityStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRecentTaskVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskTrendVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskStatusStatVO;

import java.util.List;

public interface MineMaintenanceDashboardService {

    MineMaintenanceDashboardSummaryVO getSummary();

    List<MineMaintenanceTaskStatusStatVO> getTaskStatusStats();

    List<MineMaintenancePriorityStatVO> getPriorityStats();

    List<MineMaintenanceRiskLevelStatVO> getRiskLevelStats();

    List<MineMaintenanceRiskTrendVO> getRiskTrend();

    List<MineMaintenanceRecentTaskVO> getRecentTasks();

    List<MineMaintenanceHighRiskDeviceVO> getHighRiskDevices();
}
