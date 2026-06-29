package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineDevice;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineDeviceMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper;
import cn.sxu.enterprise.module.mine.service.MineDeviceHealthService;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineDeviceHealthVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MineDeviceHealthServiceImpl implements MineDeviceHealthService {

    private static final int RISK_LEVEL_HEALTHY = 0;
    private static final int RISK_LEVEL_ATTENTION = 1;
    private static final int RISK_LEVEL_RISK = 2;
    private static final int RISK_LEVEL_HIGH_RISK = 3;

    private final MineDeviceMapper mineDeviceMapper;
    private final MineSensorMapper mineSensorMapper;
    private final MineAlarmEventMapper mineAlarmEventMapper;
    private final MineWorkOrderMapper mineWorkOrderMapper;

    public MineDeviceHealthServiceImpl(MineDeviceMapper mineDeviceMapper,
                                       MineSensorMapper mineSensorMapper,
                                       MineAlarmEventMapper mineAlarmEventMapper,
                                       MineWorkOrderMapper mineWorkOrderMapper) {
        this.mineDeviceMapper = mineDeviceMapper;
        this.mineSensorMapper = mineSensorMapper;
        this.mineAlarmEventMapper = mineAlarmEventMapper;
        this.mineWorkOrderMapper = mineWorkOrderMapper;
    }

    @Override
    public PageResult<MineDeviceHealthVO> pageDeviceHealth(MineDeviceHealthPageQuery query) {
        Long pageNo = normalizePageNo(query.getPageNo());
        Long pageSize = normalizePageSize(query.getPageSize());

        List<MineDevice> devices = mineDeviceMapper.selectList(buildDeviceWrapper(query));
        List<MineDeviceHealthVO> allRecords = devices.stream()
                .map(this::calculateDeviceHealth)
                .filter(item -> query.getRiskLevel() == null || query.getRiskLevel().equals(item.getRiskLevel()))
                .sorted(Comparator.comparing(MineDeviceHealthVO::getRiskLevel).reversed()
                        .thenComparing(MineDeviceHealthVO::getHealthScore)
                        .thenComparing(MineDeviceHealthVO::getId))
                .toList();

        long total = allRecords.size();
        long pages = total == 0 ? 0 : (total + pageSize - 1) / pageSize;
        int fromIndex = (int) Math.min((pageNo - 1) * pageSize, total);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        List<MineDeviceHealthVO> records = new ArrayList<>(allRecords.subList(fromIndex, toIndex));

        return PageResult.of(pageNo, pageSize, total, pages, records);
    }

    @Override
    public MineDeviceHealthSummaryVO getSummary() {
        List<MineDevice> devices = mineDeviceMapper.selectList(new LambdaQueryWrapper<MineDevice>()
                .orderByDesc(MineDevice::getCreateTime)
                .orderByDesc(MineDevice::getId));

        List<MineDeviceHealthVO> healthList = devices.stream()
                .map(this::calculateDeviceHealth)
                .toList();

        MineDeviceHealthSummaryVO summary = new MineDeviceHealthSummaryVO();
        summary.setDeviceTotal((long) healthList.size());
        summary.setHealthyTotal(countRiskLevel(healthList, RISK_LEVEL_HEALTHY));
        summary.setAttentionTotal(countRiskLevel(healthList, RISK_LEVEL_ATTENTION));
        summary.setRiskTotal(countRiskLevel(healthList, RISK_LEVEL_RISK));
        summary.setHighRiskTotal(countRiskLevel(healthList, RISK_LEVEL_HIGH_RISK));
        summary.setAverageHealthScore(calculateAverageHealthScore(healthList));
        summary.setSevereUnhandledAlarmTotal(healthList.stream()
                .mapToLong(MineDeviceHealthVO::getSevereUnhandledAlarmCount)
                .sum());
        summary.setUnclosedWorkOrderTotal(healthList.stream()
                .mapToLong(MineDeviceHealthVO::getUnclosedWorkOrderCount)
                .sum());
        return summary;
    }

    private LambdaQueryWrapper<MineDevice> buildDeviceWrapper(MineDeviceHealthPageQuery query) {
        LambdaQueryWrapper<MineDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getDeviceCode()), MineDevice::getDeviceCode, query.getDeviceCode())
                .like(StringUtils.hasText(query.getDeviceName()), MineDevice::getDeviceName, query.getDeviceName())
                .eq(StringUtils.hasText(query.getDeviceType()), MineDevice::getDeviceType, query.getDeviceType())
                .like(StringUtils.hasText(query.getAreaName()), MineDevice::getAreaName, query.getAreaName())
                .eq(query.getStatus() != null, MineDevice::getStatus, query.getStatus())
                .orderByDesc(MineDevice::getCreateTime)
                .orderByDesc(MineDevice::getId);
        return wrapper;
    }

    private MineDeviceHealthVO calculateDeviceHealth(MineDevice device) {
        Long deviceId = device.getId();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime alarmStartTime = now.minusHours(24);
        LocalDateTime offlineTime = now.minusMinutes(30);

        Long alarmCount24h = mineAlarmEventMapper.selectCount(new LambdaQueryWrapper<MineAlarmEvent>()
                .eq(MineAlarmEvent::getDeviceId, deviceId)
                .eq(MineAlarmEvent::getStatus, 0)
                .ge(MineAlarmEvent::getAlarmTime, alarmStartTime));

        Long severeUnhandledAlarmCount = mineAlarmEventMapper.selectCount(new LambdaQueryWrapper<MineAlarmEvent>()
                .eq(MineAlarmEvent::getDeviceId, deviceId)
                .eq(MineAlarmEvent::getAlarmLevel, 3)
                .eq(MineAlarmEvent::getHandleStatus, 0)
                .eq(MineAlarmEvent::getStatus, 0));

        Long unclosedWorkOrderCount = mineWorkOrderMapper.selectCount(new LambdaQueryWrapper<MineWorkOrder>()
                .eq(MineWorkOrder::getDeviceId, deviceId)
                .in(MineWorkOrder::getOrderStatus, 0, 1, 2)
                .eq(MineWorkOrder::getStatus, 0));

        List<MineSensor> sensors = mineSensorMapper.selectList(new LambdaQueryWrapper<MineSensor>()
                .eq(MineSensor::getDeviceId, deviceId));

        long sensorTotal = sensors.size();
        long offlineSensorCount = sensors.stream()
                .filter(sensor -> sensor.getStatus() == null
                        || sensor.getStatus() != 0
                        || sensor.getLastReportTime() == null
                        || sensor.getLastReportTime().isBefore(offlineTime))
                .count();
        LocalDateTime lastReportTime = sensors.stream()
                .map(MineSensor::getLastReportTime)
                .filter(item -> item != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        int score = 100;
        score -= Math.toIntExact(Math.min(alarmCount24h * 5, 50));
        if (severeUnhandledAlarmCount > 0) {
            score -= 20;
        }
        if (unclosedWorkOrderCount > 0) {
            score -= 10;
        }
        if (offlineSensorCount > 0) {
            score -= 10;
        }
        score = Math.max(0, Math.min(100, score));

        MineDeviceHealthVO vo = new MineDeviceHealthVO();
        vo.setId(device.getId());
        vo.setDeviceCode(device.getDeviceCode());
        vo.setDeviceName(device.getDeviceName());
        vo.setDeviceType(device.getDeviceType());
        vo.setAreaName(device.getAreaName());
        vo.setLocation(device.getLocation());
        vo.setHealthScore(score);
        vo.setRiskLevel(toRiskLevel(score));
        vo.setRiskLevelName(toRiskLevelName(score));
        vo.setAlarmCount24h(alarmCount24h);
        vo.setSevereUnhandledAlarmCount(severeUnhandledAlarmCount);
        vo.setUnclosedWorkOrderCount(unclosedWorkOrderCount);
        vo.setSensorTotal(sensorTotal);
        vo.setOfflineSensorCount(offlineSensorCount);
        vo.setLastReportTime(lastReportTime);
        vo.setStatus(device.getStatus());
        vo.setCreateTime(device.getCreateTime());
        vo.setRemark(device.getRemark());
        return vo;
    }

    private Integer toRiskLevel(int score) {
        if (score >= 90) {
            return RISK_LEVEL_HEALTHY;
        }
        if (score >= 70) {
            return RISK_LEVEL_ATTENTION;
        }
        if (score >= 50) {
            return RISK_LEVEL_RISK;
        }
        return RISK_LEVEL_HIGH_RISK;
    }

    private String toRiskLevelName(int score) {
        if (score >= 90) {
            return "健康";
        }
        if (score >= 70) {
            return "关注";
        }
        if (score >= 50) {
            return "风险";
        }
        return "高危";
    }

    private Long countRiskLevel(List<MineDeviceHealthVO> healthList, Integer riskLevel) {
        return healthList.stream()
                .filter(item -> riskLevel.equals(item.getRiskLevel()))
                .count();
    }

    private Integer calculateAverageHealthScore(List<MineDeviceHealthVO> healthList) {
        if (healthList.isEmpty()) {
            return 0;
        }
        return (int) Math.round(healthList.stream()
                .mapToInt(MineDeviceHealthVO::getHealthScore)
                .average()
                .orElse(0));
    }

    private Long normalizePageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            return 1L;
        }
        return pageNo;
    }

    private Long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 100L);
    }
}
