package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.service.MineAlarmRuleService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mine/alarm-rules")
public class MineAlarmRuleController {

    private final MineAlarmRuleService mineAlarmRuleService;

    public MineAlarmRuleController(MineAlarmRuleService mineAlarmRuleService) {
        this.mineAlarmRuleService = mineAlarmRuleService;
    }

    @OperLog(title = "智能矿山-告警规则", businessType = "分页查询")
    @GetMapping("/page")
    public ApiResult<PageResult<MineAlarmRulePageVO>> page(MineAlarmRulePageQuery query) {
        return ApiResult.success(mineAlarmRuleService.pageAlarmRules(query));
    }
}
