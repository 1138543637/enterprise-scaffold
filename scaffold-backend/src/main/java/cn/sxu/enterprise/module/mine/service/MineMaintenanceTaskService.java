package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskHandleRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskPlanRequest;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskSummaryVO;

public interface MineMaintenanceTaskService {

    PageResult<MineMaintenanceTaskPageVO> pageMaintenanceTasks(MineMaintenanceTaskPageQuery query);

    MineMaintenanceTaskSummaryVO getSummary();

    MineMaintenanceTaskPageVO createFromDeviceHealth(MineMaintenanceTaskCreateRequest request);

    MineMaintenanceTaskPageVO plan(Long id, MineMaintenanceTaskPlanRequest request);

    MineMaintenanceTaskPageVO handle(Long id, MineMaintenanceTaskHandleRequest request);

    MineMaintenanceTaskPageVO close(Long id, MineMaintenanceTaskCloseRequest request);
}
