package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineMaintenanceTask;
import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineMaintenanceTaskMapper;
import cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper;
import cn.sxu.enterprise.module.mine.service.MineMaintenanceDashboardService;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceDashboardSummaryVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceHighRiskDeviceVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenancePriorityStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRecentTaskVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskLevelStatVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceRiskTrendVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskStatusStatVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MineMaintenanceDashboardServiceImpl implements MineMaintenanceDashboardService {

    private final MineMaintenanceTaskMapper maintenanceTaskMapper;
    private final MineAlarmEventMapper alarmEventMapper;
    private final MineWorkOrderMapper workOrderMapper;

    public MineMaintenanceDashboardServiceImpl(MineMaintenanceTaskMapper maintenanceTaskMapper,
                                               MineAlarmEventMapper alarmEventMapper,
                                               MineWorkOrderMapper workOrderMapper) {
        this.maintenanceTaskMapper = maintenanceTaskMapper;
        this.alarmEventMapper = alarmEventMapper;
        this.workOrderMapper = workOrderMapper;
    }

    @Override
    public MineMaintenanceDashboardSummaryVO getSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime sevenDayStart = today.minusDays(6).atStartOfDay();

        Long taskTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0));

        Long unclosedTaskTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .ne(MineMaintenanceTask::getTaskStatus, 3));

        Long pendingTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getTaskStatus, 0));

        Long plannedTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getTaskStatus, 1));

        Long processingTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getTaskStatus, 2));

        Long closedTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getTaskStatus, 3));

        Long highRiskTaskTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getRiskLevel, 3));

        Long urgentTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .eq(MineMaintenanceTask::getPriority, 3));

        Long todayNewTaskTotal = countTask(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0)
                .ge(MineMaintenanceTask::getCreateTime, todayStart));

        Long alarmTotal7d = countAlarm(new LambdaQueryWrapper<MineAlarmEvent>()
                .eq(MineAlarmEvent::getStatus, 0)
                .ge(MineAlarmEvent::getAlarmTime, sevenDayStart));

        Long workOrderTotal7d = countWorkOrder(new LambdaQueryWrapper<MineWorkOrder>()
                .eq(MineWorkOrder::getStatus, 0)
                .ge(MineWorkOrder::getCreateTime, sevenDayStart));

        return new MineMaintenanceDashboardSummaryVO(
                taskTotal,
                unclosedTaskTotal,
                pendingTotal,
                plannedTotal,
                processingTotal,
                closedTotal,
                highRiskTaskTotal,
                urgentTotal,
                todayNewTaskTotal,
                alarmTotal7d,
                workOrderTotal7d
        );
    }

    @Override
    public List<MineMaintenanceTaskStatusStatVO> getTaskStatusStats() {
        Map<Integer, Long> countMap = new LinkedHashMap<>();
        countMap.put(0, 0L);
        countMap.put(1, 0L);
        countMap.put(2, 0L);
        countMap.put(3, 0L);

        QueryWrapper<MineMaintenanceTask> wrapper = new QueryWrapper<>();
        wrapper.select("task_status", "COUNT(1) AS total")
                .eq("status", 0)
                .groupBy("task_status");

        List<Map<String, Object>> rows = maintenanceTaskMapper.selectMaps(wrapper);
        for (Map<String, Object> row : rows) {
            Integer taskStatus = toInteger(row.get("task_status"));
            Long total = toLong(row.get("total"));
            if (taskStatus != null) {
                countMap.put(taskStatus, total);
            }
        }

        List<MineMaintenanceTaskStatusStatVO> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : countMap.entrySet()) {
            result.add(new MineMaintenanceTaskStatusStatVO(
                    entry.getKey(),
                    taskStatusName(entry.getKey()),
                    entry.getValue()
            ));
        }
        return result;
    }

    @Override
    public List<MineMaintenancePriorityStatVO> getPriorityStats() {
        Map<Integer, Long> countMap = new LinkedHashMap<>();
        countMap.put(0, 0L);
        countMap.put(1, 0L);
        countMap.put(2, 0L);
        countMap.put(3, 0L);

        QueryWrapper<MineMaintenanceTask> wrapper = new QueryWrapper<>();
        wrapper.select("priority", "COUNT(1) AS total")
                .eq("status", 0)
                .groupBy("priority");

        List<Map<String, Object>> rows = maintenanceTaskMapper.selectMaps(wrapper);
        for (Map<String, Object> row : rows) {
            Integer priority = toInteger(row.get("priority"));
            Long total = toLong(row.get("total"));
            if (priority != null) {
                countMap.put(priority, total);
            }
        }

        List<MineMaintenancePriorityStatVO> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : countMap.entrySet()) {
            result.add(new MineMaintenancePriorityStatVO(
                    entry.getKey(),
                    priorityName(entry.getKey()),
                    entry.getValue()
            ));
        }
        return result;
    }

    @Override
    public List<MineMaintenanceRiskLevelStatVO> getRiskLevelStats() {
        Map<Integer, Long> countMap = new LinkedHashMap<>();
        countMap.put(0, 0L);
        countMap.put(1, 0L);
        countMap.put(2, 0L);
        countMap.put(3, 0L);

        QueryWrapper<MineMaintenanceTask> wrapper = new QueryWrapper<>();
        wrapper.select("risk_level", "COUNT(1) AS total")
                .eq("status", 0)
                .groupBy("risk_level");

        List<Map<String, Object>> rows = maintenanceTaskMapper.selectMaps(wrapper);
        for (Map<String, Object> row : rows) {
            Integer riskLevel = toInteger(row.get("risk_level"));
            Long total = toLong(row.get("total"));
            if (riskLevel != null) {
                countMap.put(riskLevel, total);
            }
        }

        List<MineMaintenanceRiskLevelStatVO> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : countMap.entrySet()) {
            result.add(new MineMaintenanceRiskLevelStatVO(
                    entry.getKey(),
                    riskLevelName(entry.getKey()),
                    entry.getValue()
            ));
        }
        return result;
    }

    @Override
    public List<MineMaintenanceRiskTrendVO> getRiskTrend() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);
        LocalDateTime startTime = startDate.atStartOfDay();

        Map<LocalDate, TrendCounter> trendMap = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            trendMap.put(date, new TrendCounter(date));
        }

        List<MineAlarmEvent> alarms = alarmEventMapper.selectList(
                new LambdaQueryWrapper<MineAlarmEvent>()
                        .eq(MineAlarmEvent::getStatus, 0)
                        .ge(MineAlarmEvent::getAlarmTime, startTime)
        );

        for (MineAlarmEvent alarm : alarms) {
            if (alarm.getAlarmTime() == null) {
                continue;
            }
            LocalDate date = alarm.getAlarmTime().toLocalDate();
            TrendCounter counter = trendMap.get(date);
            if (counter != null) {
                counter.alarmTotal++;
            }
        }

        List<MineWorkOrder> workOrders = workOrderMapper.selectList(
                new LambdaQueryWrapper<MineWorkOrder>()
                        .eq(MineWorkOrder::getStatus, 0)
                        .ge(MineWorkOrder::getCreateTime, startTime)
        );

        for (MineWorkOrder workOrder : workOrders) {
            if (workOrder.getCreateTime() == null) {
                continue;
            }
            LocalDate date = workOrder.getCreateTime().toLocalDate();
            TrendCounter counter = trendMap.get(date);
            if (counter != null) {
                counter.workOrderTotal++;
            }
        }

        List<MineMaintenanceTask> tasks = maintenanceTaskMapper.selectList(
                new LambdaQueryWrapper<MineMaintenanceTask>()
                        .eq(MineMaintenanceTask::getStatus, 0)
                        .ge(MineMaintenanceTask::getCreateTime, startTime)
        );

        for (MineMaintenanceTask task : tasks) {
            if (task.getCreateTime() == null) {
                continue;
            }
            LocalDate date = task.getCreateTime().toLocalDate();
            TrendCounter counter = trendMap.get(date);
            if (counter != null) {
                counter.maintenanceTaskTotal++;
                if (Integer.valueOf(3).equals(task.getRiskLevel())) {
                    counter.highRiskTaskTotal++;
                }
            }
        }

        List<MineMaintenanceRiskTrendVO> result = new ArrayList<>();
        for (TrendCounter counter : trendMap.values()) {
            result.add(new MineMaintenanceRiskTrendVO(
                    counter.date.toString(),
                    counter.alarmTotal,
                    counter.workOrderTotal,
                    counter.maintenanceTaskTotal,
                    counter.highRiskTaskTotal
            ));
        }
        return result;
    }

    @Override
    public List<MineMaintenanceRecentTaskVO> getRecentTasks() {
        List<MineMaintenanceTask> tasks = maintenanceTaskMapper.selectList(
                new LambdaQueryWrapper<MineMaintenanceTask>()
                        .eq(MineMaintenanceTask::getStatus, 0)
                        .orderByDesc(MineMaintenanceTask::getCreateTime)
                        .orderByDesc(MineMaintenanceTask::getId)
                        .last("LIMIT 10")
        );

        List<MineMaintenanceRecentTaskVO> result = new ArrayList<>();
        for (MineMaintenanceTask task : tasks) {
            result.add(new MineMaintenanceRecentTaskVO(
                    task.getId(),
                    task.getTaskCode(),
                    task.getDeviceCode(),
                    task.getDeviceName(),
                    task.getDeviceType(),
                    task.getAreaName(),
                    task.getHealthScore(),
                    task.getRiskLevel(),
                    riskLevelName(task.getRiskLevel()),
                    task.getTaskStatus(),
                    taskStatusName(task.getTaskStatus()),
                    task.getPriority(),
                    priorityName(task.getPriority()),
                    task.getCreateTime()
            ));
        }
        return result;
    }

    @Override
    public List<MineMaintenanceHighRiskDeviceVO> getHighRiskDevices() {
        List<MineMaintenanceTask> tasks = maintenanceTaskMapper.selectList(
                new LambdaQueryWrapper<MineMaintenanceTask>()
                        .eq(MineMaintenanceTask::getStatus, 0)
                        .eq(MineMaintenanceTask::getRiskLevel, 3)
                        .ne(MineMaintenanceTask::getTaskStatus, 3)
                        .orderByDesc(MineMaintenanceTask::getPriority)
                        .orderByDesc(MineMaintenanceTask::getCreateTime)
                        .orderByDesc(MineMaintenanceTask::getId)
                        .last("LIMIT 10")
        );

        List<MineMaintenanceHighRiskDeviceVO> result = new ArrayList<>();
        for (MineMaintenanceTask task : tasks) {
            result.add(new MineMaintenanceHighRiskDeviceVO(
                    task.getId(),
                    task.getTaskCode(),
                    task.getDeviceId(),
                    task.getDeviceCode(),
                    task.getDeviceName(),
                    task.getDeviceType(),
                    task.getAreaName(),
                    task.getLocation(),
                    task.getHealthScore(),
                    task.getRiskLevel(),
                    riskLevelName(task.getRiskLevel()),
                    task.getTaskStatus(),
                    taskStatusName(task.getTaskStatus()),
                    task.getPriority(),
                    priorityName(task.getPriority()),
                    task.getCreateTime()
            ));
        }
        return result;
    }

    private Long countTask(LambdaQueryWrapper<MineMaintenanceTask> wrapper) {
        Long count = maintenanceTaskMapper.selectCount(wrapper);
        return count == null ? 0L : count;
    }

    private Long countAlarm(LambdaQueryWrapper<MineAlarmEvent> wrapper) {
        Long count = alarmEventMapper.selectCount(wrapper);
        return count == null ? 0L : count;
    }

    private Long countWorkOrder(LambdaQueryWrapper<MineWorkOrder> wrapper) {
        Long count = workOrderMapper.selectCount(wrapper);
        return count == null ? 0L : count;
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private String taskStatusName(Integer taskStatus) {
        if (taskStatus == null) {
            return "未知";
        }
        return switch (taskStatus) {
            case 0 -> "待安排";
            case 1 -> "待执行";
            case 2 -> "处理中";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    private String priorityName(Integer priority) {
        if (priority == null) {
            return "未知";
        }
        return switch (priority) {
            case 0 -> "低";
            case 1 -> "中";
            case 2 -> "高";
            case 3 -> "紧急";
            default -> "未知";
        };
    }

    private String riskLevelName(Integer riskLevel) {
        if (riskLevel == null) {
            return "未知";
        }
        return switch (riskLevel) {
            case 0 -> "健康";
            case 1 -> "关注";
            case 2 -> "风险";
            case 3 -> "高危";
            default -> "未知";
        };
    }

    private static class TrendCounter {
        private final LocalDate date;
        private long alarmTotal;
        private long workOrderTotal;
        private long maintenanceTaskTotal;
        private long highRiskTaskTotal;

        private TrendCounter(LocalDate date) {
            this.date = date;
        }
    }
}
