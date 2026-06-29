package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.dto.MineAlarmGenerateRequest;
import cn.sxu.enterprise.module.mine.service.MineAlarmEventService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/alarm-events")
public class MineAlarmEventController {

    private final MineAlarmEventService mineAlarmEventService;

    public MineAlarmEventController(MineAlarmEventService mineAlarmEventService) {
        this.mineAlarmEventService = mineAlarmEventService;
    }

    @OperLog(title = "智能矿山-告警事件", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineAlarmEventPageVO>> page(MineAlarmEventPageQuery query) {
        return ApiResult.success(mineAlarmEventService.pageAlarmEvents(query));
    }

    @OperLog(title = "智能矿山-告警事件", businessType = "生成告警事件")
    @PostMapping("/generate")
    public ApiResult<?> generate(@Valid @RequestBody(required = false) MineAlarmGenerateRequest request) {
        return ApiResult.success(mineAlarmEventService.generate(request));
    }
}
