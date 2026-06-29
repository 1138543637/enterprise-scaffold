package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineAlarmGenerateRequest;
import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineAlarmRule;
import cn.sxu.enterprise.module.mine.entity.MineSensorData;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmRuleMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorDataMapper;
import cn.sxu.enterprise.module.mine.service.MineAlarmEventService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineAlarmEventPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MineAlarmEventServiceImpl implements MineAlarmEventService {

    private final MineAlarmEventMapper mineAlarmEventMapper;

    private final MineAlarmRuleMapper mineAlarmRuleMapper;

    private final MineSensorDataMapper mineSensorDataMapper;

    public MineAlarmEventServiceImpl(MineAlarmEventMapper mineAlarmEventMapper,
                                     MineAlarmRuleMapper mineAlarmRuleMapper,
                                     MineSensorDataMapper mineSensorDataMapper) {
        this.mineAlarmEventMapper = mineAlarmEventMapper;
        this.mineAlarmRuleMapper = mineAlarmRuleMapper;
        this.mineSensorDataMapper = mineSensorDataMapper;
    }

    @Override
    public PageResult<MineAlarmEventPageVO> pageAlarmEvents(MineAlarmEventPageQuery query) {
        if (query == null) {
            query = new MineAlarmEventPageQuery();
        }

        Long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10L : query.getPageSize();

        Page<MineAlarmEvent> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<MineAlarmEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getEventCode()), MineAlarmEvent::getEventCode, query.getEventCode());
        wrapper.like(StringUtils.hasText(query.getRuleCode()), MineAlarmEvent::getRuleCode, query.getRuleCode());
        wrapper.like(StringUtils.hasText(query.getSensorCode()), MineAlarmEvent::getSensorCode, query.getSensorCode());
        wrapper.like(StringUtils.hasText(query.getSensorName()), MineAlarmEvent::getSensorName, query.getSensorName());
        wrapper.eq(StringUtils.hasText(query.getSensorType()), MineAlarmEvent::getSensorType, query.getSensorType());
        wrapper.like(StringUtils.hasText(query.getAreaName()), MineAlarmEvent::getAreaName, query.getAreaName());
        wrapper.eq(query.getAlarmLevel() != null, MineAlarmEvent::getAlarmLevel, query.getAlarmLevel());
        wrapper.eq(query.getHandleStatus() != null, MineAlarmEvent::getHandleStatus, query.getHandleStatus());
        wrapper.eq(query.getStatus() != null, MineAlarmEvent::getStatus, query.getStatus());
        wrapper.orderByDesc(MineAlarmEvent::getAlarmTime);
        wrapper.orderByDesc(MineAlarmEvent::getId);

        Page<MineAlarmEvent> result = mineAlarmEventMapper.selectPage(page, wrapper);

        List<MineAlarmEventPageVO> records = result.getRecords()
                .stream()
                .map(MineAlarmEventPageVO::fromEntity)
                .toList();

        return PageResult.of(
                result.getCurrent(),
                result.getSize(),
                result.getTotal(),
                result.getPages(),
                records
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MineAlarmEventPageVO> generate(MineAlarmGenerateRequest request) {
        if (request == null) {
            request = new MineAlarmGenerateRequest();
        }

        int limit = request.getLimit() == null ? 100 : request.getLimit();
        limit = Math.max(1, Math.min(limit, 1000));

        List<MineSensorData> sensorDataList = querySensorData(request, limit);
        if (sensorDataList.isEmpty()) {
            return List.of();
        }

        List<MineAlarmRule> ruleList = queryEnabledRules();
        if (ruleList.isEmpty()) {
            return List.of();
        }

        Map<String, List<MineAlarmRule>> ruleMap = ruleList.stream()
                .collect(Collectors.groupingBy(MineAlarmRule::getSensorType));

        List<MineAlarmEventPageVO> generatedEvents = new ArrayList<>();

        for (MineSensorData sensorData : sensorDataList) {
            List<MineAlarmRule> matchedRules = ruleMap.get(sensorData.getSensorType());
            if (matchedRules == null || matchedRules.isEmpty()) {
                continue;
            }

            for (MineAlarmRule rule : matchedRules) {
                if (!matchRule(sensorData.getDataValue(), rule.getThresholdValue(), rule.getCompareOperator())) {
                    continue;
                }

                if (alarmEventExists(rule.getId(), sensorData.getId())) {
                    continue;
                }

                MineAlarmEvent event = buildAlarmEvent(rule, sensorData);
                mineAlarmEventMapper.insert(event);
                generatedEvents.add(MineAlarmEventPageVO.fromEntity(event));
            }
        }

        return generatedEvents;
    }

    private List<MineSensorData> querySensorData(MineAlarmGenerateRequest request, int limit) {
        LambdaQueryWrapper<MineSensorData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(request.getSensorDataId() != null, MineSensorData::getId, request.getSensorDataId());
        wrapper.eq(StringUtils.hasText(request.getSensorType()), MineSensorData::getSensorType, request.getSensorType());
        wrapper.orderByDesc(MineSensorData::getCollectTime);
        wrapper.orderByDesc(MineSensorData::getId);
        wrapper.last("LIMIT " + limit);

        return mineSensorDataMapper.selectList(wrapper);
    }

    private List<MineAlarmRule> queryEnabledRules() {
        LambdaQueryWrapper<MineAlarmRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MineAlarmRule::getStatus, 0);
        return mineAlarmRuleMapper.selectList(wrapper);
    }

    private boolean alarmEventExists(Long ruleId, Long sensorDataId) {
        LambdaQueryWrapper<MineAlarmEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MineAlarmEvent::getRuleId, ruleId);
        wrapper.eq(MineAlarmEvent::getSensorDataId, sensorDataId);
        return mineAlarmEventMapper.selectCount(wrapper) > 0;
    }

    private boolean matchRule(BigDecimal dataValue, BigDecimal thresholdValue, String compareOperator) {
        if (dataValue == null || thresholdValue == null || !StringUtils.hasText(compareOperator)) {
            return false;
        }

        int compareResult = dataValue.compareTo(thresholdValue);

        return switch (compareOperator) {
            case "GT" -> compareResult > 0;
            case "GE" -> compareResult >= 0;
            case "LT" -> compareResult < 0;
            case "LE" -> compareResult <= 0;
            case "EQ" -> compareResult == 0;
            default -> false;
        };
    }

    private MineAlarmEvent buildAlarmEvent(MineAlarmRule rule, MineSensorData sensorData) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime alarmTime = sensorData.getCollectTime() == null ? now : sensorData.getCollectTime();

        String eventCode = "ALM-D" + sensorData.getId() + "-R" + rule.getId();

        String alarmContent = buildAlarmContent(rule, sensorData);

        MineAlarmEvent event = new MineAlarmEvent();
        event.setEventCode(eventCode);
        event.setRuleId(rule.getId());
        event.setRuleCode(rule.getRuleCode());
        event.setRuleName(rule.getRuleName());
        event.setSensorDataId(sensorData.getId());
        event.setSensorId(sensorData.getSensorId());
        event.setSensorCode(sensorData.getSensorCode());
        event.setSensorName(sensorData.getSensorName());
        event.setSensorType(sensorData.getSensorType());
        event.setDeviceId(sensorData.getDeviceId());
        event.setAreaName(sensorData.getAreaName());
        event.setLocation(sensorData.getLocation());
        event.setDataValue(sensorData.getDataValue());
        event.setThresholdValue(rule.getThresholdValue());
        event.setCompareOperator(rule.getCompareOperator());
        event.setAlarmLevel(rule.getAlarmLevel());
        event.setAlarmTitle(rule.getAlarmTitle());
        event.setAlarmContent(alarmContent);
        event.setAlarmTime(alarmTime);
        event.setHandleStatus(0);
        event.setStatus(0);
        event.setCreateBy("system");
        event.setCreateTime(now);
        event.setDeleted(0);
        event.setRemark("M1-04 根据传感器数据自动生成告警事件");
        return event;
    }

    private String buildAlarmContent(MineAlarmRule rule, MineSensorData sensorData) {
        String unit = sensorData.getUnit() == null ? "" : sensorData.getUnit();

        return sensorData.getSensorName()
                + " 采集值 "
                + sensorData.getDataValue()
                + unit
                + " 触发规则【"
                + rule.getRuleName()
                + "】，规则阈值为 "
                + rule.getThresholdValue()
                + unit
                + "。";
    }
}
