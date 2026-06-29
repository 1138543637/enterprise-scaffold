package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthVO;

public interface MineDeviceHealthService {

    PageResult<MineDeviceHealthVO> pageDeviceHealth(MineDeviceHealthPageQuery query);

    MineDeviceHealthSummaryVO getSummary();
}
