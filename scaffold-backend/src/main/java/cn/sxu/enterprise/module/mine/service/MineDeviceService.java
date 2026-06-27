package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageVO;

public interface MineDeviceService {

    PageResult<MineDevicePageVO> pageDevices(MineDevicePageQuery query);
}