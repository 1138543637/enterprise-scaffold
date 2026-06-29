package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageVO;

public interface MineAlarmRuleService {

    PageResult<MineAlarmRulePageVO> pageAlarmRules(MineAlarmRulePageQuery query);
}
