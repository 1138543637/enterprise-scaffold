package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.entity.MineAlarmRule;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmRuleMapper;
import cn.sxu.enterprise.module.mine.service.MineAlarmRuleService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmRulePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MineAlarmRuleServiceImpl implements MineAlarmRuleService {

    private final MineAlarmRuleMapper mineAlarmRuleMapper;

    public MineAlarmRuleServiceImpl(MineAlarmRuleMapper mineAlarmRuleMapper) {
        this.mineAlarmRuleMapper = mineAlarmRuleMapper;
    }

    @Override
    public PageResult<MineAlarmRulePageVO> pageAlarmRules(MineAlarmRulePageQuery query) {
        if (query == null) {
            query = new MineAlarmRulePageQuery();
        }

        Long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10L : query.getPageSize();

        Page<MineAlarmRule> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<MineAlarmRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRuleCode()), MineAlarmRule::getRuleCode, query.getRuleCode());
        wrapper.like(StringUtils.hasText(query.getRuleName()), MineAlarmRule::getRuleName, query.getRuleName());
        wrapper.eq(StringUtils.hasText(query.getSensorType()), MineAlarmRule::getSensorType, query.getSensorType());
        wrapper.eq(query.getAlarmLevel() != null, MineAlarmRule::getAlarmLevel, query.getAlarmLevel());
        wrapper.eq(query.getStatus() != null, MineAlarmRule::getStatus, query.getStatus());
        wrapper.orderByAsc(MineAlarmRule::getSensorType);
        wrapper.orderByAsc(MineAlarmRule::getAlarmLevel);
        wrapper.orderByDesc(MineAlarmRule::getId);

        Page<MineAlarmRule> result = mineAlarmRuleMapper.selectPage(page, wrapper);

        List<MineAlarmRulePageVO> records = result.getRecords()
                .stream()
                .map(MineAlarmRulePageVO::fromEntity)
                .toList();

        return PageResult.of(
                result.getCurrent(),
                result.getSize(),
                result.getTotal(),
                result.getPages(),
                records
        );
    }
}
