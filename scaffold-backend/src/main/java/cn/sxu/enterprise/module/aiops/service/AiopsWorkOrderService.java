package cn.sxu.enterprise.module.aiops.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCloseRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCreateRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderHandleRequest;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageVO;

public interface AiopsWorkOrderService {
    PageResult<AiopsWorkOrderPageVO> pageWorkOrders(AiopsWorkOrderPageQuery query);
    AiopsWorkOrderPageVO createFromAlert(AiopsWorkOrderCreateRequest request);
    AiopsWorkOrderPageVO handle(Long id, AiopsWorkOrderHandleRequest request);
    AiopsWorkOrderPageVO close(Long id, AiopsWorkOrderCloseRequest request);
}
