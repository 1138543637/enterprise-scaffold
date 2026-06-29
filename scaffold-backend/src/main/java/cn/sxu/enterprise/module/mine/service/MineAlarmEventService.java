package cn.sxu.enterprise.module.mine.service;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineAlarmGenerateRequest;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageVO;

import java.util.List;

public interface MineAlarmEventService {

    PageResult<MineAlarmEventPageVO> pageAlarmEvents(MineAlarmEventPageQuery query);

    List<MineAlarmEventPageVO> generate(MineAlarmGenerateRequest request);
}
