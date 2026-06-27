package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageVO;

public interface MineSensorService {

    PageResult<MineSensorPageVO> pageSensors(MineSensorPageQuery query);
}