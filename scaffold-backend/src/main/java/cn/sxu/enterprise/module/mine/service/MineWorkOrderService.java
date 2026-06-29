package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderHandleRequest;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageVO;

public interface MineWorkOrderService {

    PageResult<MineWorkOrderPageVO> pageWorkOrders(MineWorkOrderPageQuery query);

    MineWorkOrderPageVO createFromAlarm(MineWorkOrderCreateRequest request);

    MineWorkOrderPageVO handle(Long id, MineWorkOrderHandleRequest request);

    MineWorkOrderPageVO close(Long id, MineWorkOrderCloseRequest request);
}
