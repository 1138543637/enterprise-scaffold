package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineSensorDataSimulateRequest;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataVO;

import java.util.List;

public interface MineSensorDataService {

    List<MineSensorDataVO> simulate(MineSensorDataSimulateRequest request);

    List<MineSensorDataVO> latest(MineSensorDataPageQuery query);

    PageResult<MineSensorDataVO> pageSensorData(MineSensorDataPageQuery query);
}