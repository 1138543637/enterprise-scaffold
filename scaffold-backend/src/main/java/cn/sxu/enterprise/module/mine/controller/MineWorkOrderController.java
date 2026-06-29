package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderHandleRequest;
import cn.sxu.enterprise.module.mine.service.MineWorkOrderService;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mine/work-orders")
public class MineWorkOrderController {

    private final MineWorkOrderService mineWorkOrderService;

    public MineWorkOrderController(MineWorkOrderService mineWorkOrderService) {
        this.mineWorkOrderService = mineWorkOrderService;
    }

    @OperLog(title = "智能矿山-工单", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineWorkOrderPageVO>> page(MineWorkOrderPageQuery query) {
        return ApiResult.success(mineWorkOrderService.pageWorkOrders(query));
    }

    @OperLog(title = "智能矿山-工单", businessType = "告警转工单")
    @PostMapping("/create-from-alarm")
    public ApiResult<MineWorkOrderPageVO> createFromAlarm(@Valid @RequestBody MineWorkOrderCreateRequest request) {
        return ApiResult.success(mineWorkOrderService.createFromAlarm(request));
    }

    @OperLog(title = "智能矿山-工单", businessType = "工单处理")
    @PostMapping("/{id}/handle")
    public ApiResult<MineWorkOrderPageVO> handle(@PathVariable Long id,
                                                  @Valid @RequestBody MineWorkOrderHandleRequest request) {
        return ApiResult.success(mineWorkOrderService.handle(id, request));
    }

    @OperLog(title = "智能矿山-工单", businessType = "工单关闭")
    @PostMapping("/{id}/close")
    public ApiResult<MineWorkOrderPageVO> close(@PathVariable Long id,
                                                 @Valid @RequestBody MineWorkOrderCloseRequest request) {
        return ApiResult.success(mineWorkOrderService.close(id, request));
    }
}
