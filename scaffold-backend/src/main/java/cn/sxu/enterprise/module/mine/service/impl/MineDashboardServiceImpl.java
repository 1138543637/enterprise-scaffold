package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineAlarmRule;
import cn.sxu.enterprise.module.mine.entity.MineDevice;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.entity.MineSensorData;
import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmRuleMapper;
import cn.sxu.enterprise.module.mine.mapper.MineDeviceMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorDataMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper;
import cn.sxu.enterprise.module.mine.service.MineDashboardService;
import cn.sxu.enterprise.module.mine.vo.MineAlarmLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentAlarmVO;
import cn.sxu.enterprise.module.mine.vo.MineRecentWorkOrderVO;
import cn.sxu.enterprise.module.mine.vo.MineSensorTypeStatVO;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderStatusStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MineDashboardServiceImpl implements MineDashboardService {

    private static final int HANDLE_STATUS_UNHANDLED = 0;
    private static final int ORDER_STATUS_PENDING = 0;
    private static final int ORDER_STATUS_CLOSED = 3;
    private static final int RECENT_LIMIT = 10;

    private final MineDeviceMapper mineDeviceMapper;
    private final MineSensorMapper mineSensorMapper;
    private final MineSensorDataMapper mineSensorDataMapper;
    private final MineAlarmRuleMapper mineAlarmRuleMapper;
    private final MineAlarmEventMapper mineAlarmEventMapper;
    private final MineWorkOrderMapper mineWorkOrderMapper;

    public MineDashboardServiceImpl(MineDeviceMapper mineDeviceMapper,
                                    MineSensorMapper mineSensorMapper,
                                    MineSensorDataMapper mineSensorDataMapper,
                                    MineAlarmRuleMapper mineAlarmRuleMapper,
                                    MineAlarmEventMapper mineAlarmEventMapper,
                                    MineWorkOrderMapper mineWorkOrderMapper) {
        this.mineDeviceMapper = mineDeviceMapper;
        this.mineSensorMapper = mineSensorMapper;
        this.mineSensorDataMapper = mineSensorDataMapper;
        this.mineAlarmRuleMapper = mineAlarmRuleMapper;
        this.mineAlarmEventMapper = mineAlarmEventMapper;
        this.mineWorkOrderMapper = mineWorkOrderMapper;
    }

    @Override
    public MineDashboardSummaryVO getSummary() {
        Long deviceTotal = mineDeviceMapper.selectCount(new LambdaQueryWrapper<MineDevice>());
        Long sensorTotal = mineSensorMapper.selectCount(new LambdaQueryWrapper<MineSensor>());
        Long sensorDataTotal = mineSensorDataMapper.selectCount(new LambdaQueryWrapper<MineSensorData>());
        Long alarmRuleTotal = mineAlarmRuleMapper.selectCount(new LambdaQueryWrapper<MineAlarmRule>());
        Long alarmEventTotal = mineAlarmEventMapper.selectCount(new LambdaQueryWrapper<MineAlarmEvent>());

        Long unhandledAlarmTotal = mineAlarmEventMapper.selectCount(
                new LambdaQueryWrapper<MineAlarmEvent>()
                        .eq(MineAlarmEvent::getHandleStatus, HANDLE_STATUS_UNHANDLED)
        );

        Long workOrderTotal = mineWorkOrderMapper.selectCount(new LambdaQueryWrapper<MineWorkOrder>());

        Long pendingWorkOrderTotal = mineWorkOrderMapper.selectCount(
                new LambdaQueryWrapper<MineWorkOrder>()
                        .eq(MineWorkOrder::getOrderStatus, ORDER_STATUS_PENDING)
        );

        Long closedWorkOrderTotal = mineWorkOrderMapper.selectCount(
                new LambdaQueryWrapper<MineWorkOrder>()
                        .eq(MineWorkOrder::getOrderStatus, ORDER_STATUS_CLOSED)
        );

        return new MineDashboardSummaryVO(
                deviceTotal,
                sensorTotal,
                sensorDataTotal,
                alarmRuleTotal,
                alarmEventTotal,
                unhandledAlarmTotal,
                workOrderTotal,
                pendingWorkOrderTotal,
                closedWorkOrderTotal
        );
    }

    @Override
    public List<MineAlarmLevelStatVO> getAlarmLevelStats() {
        QueryWrapper<MineAlarmEvent> wrapper = new QueryWrapper<>();
        wrapper.select("alarm_level AS alarmLevel", "COUNT(1) AS total")
                .eq("deleted", 0)
                .groupBy("alarm_level")
                .orderByAsc("alarm_level");

        return mineAlarmEventMapper.selectMaps(wrapper).stream()
                .map(map -> {
                    Integer alarmLevel = getInteger(map, "alarmLevel", "alarm_level");
                    Long total = getLong(map, "total", "total");
                    return new MineAlarmLevelStatVO(alarmLevel, getAlarmLevelName(alarmLevel), total);
                })
                .toList();
    }

    @Override
    public List<MineSensorTypeStatVO> getSensorTypeStats() {
        QueryWrapper<MineSensor> wrapper = new QueryWrapper<>();
        wrapper.select("sensor_type AS sensorType", "COUNT(1) AS total")
                .eq("deleted", 0)
                .groupBy("sensor_type")
                .orderByAsc("sensor_type");

        return mineSensorMapper.selectMaps(wrapper).stream()
                .map(map -> {
                    String sensorType = getString(map, "sensorType", "sensor_type");
                    Long total = getLong(map, "total", "total");
                    return new MineSensorTypeStatVO(sensorType, getSensorTypeName(sensorType), total);
                })
                .toList();
    }

    @Override
    public List<MineWorkOrderStatusStatVO> getWorkOrderStatusStats() {
        QueryWrapper<MineWorkOrder> wrapper = new QueryWrapper<>();
        wrapper.select("order_status AS orderStatus", "COUNT(1) AS total")
                .eq("deleted", 0)
                .groupBy("order_status")
                .orderByAsc("order_status");

        return mineWorkOrderMapper.selectMaps(wrapper).stream()
                .map(map -> {
                    Integer orderStatus = getInteger(map, "orderStatus", "order_status");
                    Long total = getLong(map, "total", "total");
                    return new MineWorkOrderStatusStatVO(orderStatus, getOrderStatusName(orderStatus), total);
                })
                .toList();
    }

    @Override
    public List<MineRecentAlarmVO> getRecentAlarms() {
        List<MineAlarmEvent> alarms = mineAlarmEventMapper.selectList(
                new LambdaQueryWrapper<MineAlarmEvent>()
                        .orderByDesc(MineAlarmEvent::getAlarmTime)
                        .orderByDesc(MineAlarmEvent::getId)
                        .last("LIMIT " + RECENT_LIMIT)
        );

        return alarms.stream()
                .map(this::toRecentAlarmVO)
                .toList();
    }

    @Override
    public List<MineRecentWorkOrderVO> getRecentWorkOrders() {
        List<MineWorkOrder> workOrders = mineWorkOrderMapper.selectList(
                new LambdaQueryWrapper<MineWorkOrder>()
                        .orderByDesc(MineWorkOrder::getCreateTime)
                        .orderByDesc(MineWorkOrder::getId)
                        .last("LIMIT " + RECENT_LIMIT)
        );

        return workOrders.stream()
                .map(this::toRecentWorkOrderVO)
                .toList();
    }

    private MineRecentAlarmVO toRecentAlarmVO(MineAlarmEvent entity) {
        return new MineRecentAlarmVO(
                entity.getId(),
                entity.getEventCode(),
                entity.getSensorCode(),
                entity.getSensorName(),
                entity.getSensorType(),
                entity.getAreaName(),
                entity.getLocation(),
                entity.getDataValue(),
                entity.getThresholdValue(),
                entity.getAlarmLevel(),
                getAlarmLevelName(entity.getAlarmLevel()),
                entity.getHandleStatus(),
                getHandleStatusName(entity.getHandleStatus()),
                entity.getAlarmTime()
        );
    }

    private MineRecentWorkOrderVO toRecentWorkOrderVO(MineWorkOrder entity) {
        return new MineRecentWorkOrderVO(
                entity.getId(),
                entity.getWorkOrderCode(),
                entity.getEventCode(),
                entity.getSensorCode(),
                entity.getSensorName(),
                entity.getSensorType(),
                entity.getAreaName(),
                entity.getLocation(),
                entity.getAlarmLevel(),
                getAlarmLevelName(entity.getAlarmLevel()),
                entity.getOrderStatus(),
                getOrderStatusName(entity.getOrderStatus()),
                entity.getCreateTime()
        );
    }

    private String getAlarmLevelName(Integer alarmLevel) {
        if (alarmLevel == null) {
            return "未知";
        }
        return switch (alarmLevel) {
            case 1 -> "一般";
            case 2 -> "重要";
            case 3 -> "严重";
            default -> "未知";
        };
    }

    private String getHandleStatusName(Integer handleStatus) {
        if (handleStatus == null) {
            return "未知";
        }
        return switch (handleStatus) {
            case 0 -> "未处理";
            case 1 -> "已确认";
            case 2 -> "已关闭";
            default -> "未知";
        };
    }

    private String getOrderStatusName(Integer orderStatus) {
        if (orderStatus == null) {
            return "未知";
        }
        return switch (orderStatus) {
            case 0 -> "待处理";
            case 1 -> "处理中";
            case 2 -> "已处理";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    private String getSensorTypeName(String sensorType) {
        if (sensorType == null || sensorType.isBlank()) {
            return "未知";
        }
        return switch (sensorType) {
            case "GAS" -> "瓦斯";
            case "TEMPERATURE" -> "温度";
            case "VIBRATION" -> "振动";
            default -> sensorType;
        };
    }

    private Integer getInteger(Map<String, Object> map, String camelKey, String underlineKey) {
        Object value = getValue(map, camelKey, underlineKey);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(value.toString());
    }

    private Long getLong(Map<String, Object> map, String camelKey, String underlineKey) {
        Object value = getValue(map, camelKey, underlineKey);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(value.toString());
    }

    private String getString(Map<String, Object> map, String camelKey, String underlineKey) {
        Object value = getValue(map, camelKey, underlineKey);
        return value == null ? null : value.toString();
    }

    private Object getValue(Map<String, Object> map, String camelKey, String underlineKey) {
        Object value = map.get(camelKey);
        if (value == null) {
            value = map.get(underlineKey);
        }
        return value;
    }
}
