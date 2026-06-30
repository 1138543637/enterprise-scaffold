package cn.sxu.enterprise.module.aiops.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCloseRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCreateRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderHandleRequest;
import cn.sxu.enterprise.module.aiops.service.AiopsWorkOrderService;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aiops/work-orders")
public class AiopsWorkOrderController {
    private final AiopsWorkOrderService aiopsWorkOrderService;
    public AiopsWorkOrderController(AiopsWorkOrderService aiopsWorkOrderService) { this.aiopsWorkOrderService = aiopsWorkOrderService; }
    @OperLog(title = "AIOps运维工单", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<AiopsWorkOrderPageVO>> page(AiopsWorkOrderPageQuery query) { return ApiResult.success(aiopsWorkOrderService.pageWorkOrders(query)); }
    @OperLog(title = "AIOps运维工单", businessType = "告警转工单")
    @PostMapping("/create-from-alert")
    public ApiResult<AiopsWorkOrderPageVO> createFromAlert(@Valid @RequestBody AiopsWorkOrderCreateRequest request) { return ApiResult.success(aiopsWorkOrderService.createFromAlert(request)); }
    @OperLog(title = "AIOps运维工单", businessType = "工单处理")
    @PostMapping("/{id}/handle")
    public ApiResult<AiopsWorkOrderPageVO> handle(@PathVariable Long id, @Valid @RequestBody AiopsWorkOrderHandleRequest request) { return ApiResult.success(aiopsWorkOrderService.handle(id, request)); }
    @OperLog(title = "AIOps运维工单", businessType = "工单关闭")
    @PostMapping("/{id}/close")
    public ApiResult<AiopsWorkOrderPageVO> close(@PathVariable Long id, @Valid @RequestBody AiopsWorkOrderCloseRequest request) { return ApiResult.success(aiopsWorkOrderService.close(id, request)); }
}
