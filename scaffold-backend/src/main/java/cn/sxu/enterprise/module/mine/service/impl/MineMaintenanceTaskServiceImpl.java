package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskHandleRequest;
import cn.sxu.enterprise.module.mine.dto.MineMaintenanceTaskPlanRequest;
import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineDevice;
import cn.sxu.enterprise.module.mine.entity.MineMaintenanceTask;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineDeviceMapper;
import cn.sxu.enterprise.module.mine.mapper.MineMaintenanceTaskMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper;
import cn.sxu.enterprise.module.mine.service.MineMaintenanceTaskService;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskPageVO;
import cn.sxu.enterprise.module.mine.vo.MineMaintenanceTaskSummaryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MineMaintenanceTaskServiceImpl implements MineMaintenanceTaskService {

    private static final int TASK_STATUS_PENDING = 0;
    private static final int TASK_STATUS_PLANNED = 1;
    private static final int TASK_STATUS_PROCESSING = 2;
    private static final int TASK_STATUS_CLOSED = 3;

    private static final int RISK_LEVEL_HEALTHY = 0;
    private static final int RISK_LEVEL_ATTENTION = 1;
    private static final int RISK_LEVEL_RISK = 2;
    private static final int RISK_LEVEL_HIGH_RISK = 3;

    private final MineMaintenanceTaskMapper mineMaintenanceTaskMapper;
    private final MineDeviceMapper mineDeviceMapper;
    private final MineSensorMapper mineSensorMapper;
    private final MineAlarmEventMapper mineAlarmEventMapper;
    private final MineWorkOrderMapper mineWorkOrderMapper;

    public MineMaintenanceTaskServiceImpl(MineMaintenanceTaskMapper mineMaintenanceTaskMapper,
                                          MineDeviceMapper mineDeviceMapper,
                                          MineSensorMapper mineSensorMapper,
                                          MineAlarmEventMapper mineAlarmEventMapper,
                                          MineWorkOrderMapper mineWorkOrderMapper) {
        this.mineMaintenanceTaskMapper = mineMaintenanceTaskMapper;
        this.mineDeviceMapper = mineDeviceMapper;
        this.mineSensorMapper = mineSensorMapper;
        this.mineAlarmEventMapper = mineAlarmEventMapper;
        this.mineWorkOrderMapper = mineWorkOrderMapper;
    }

    @Override
    public PageResult<MineMaintenanceTaskPageVO> pageMaintenanceTasks(MineMaintenanceTaskPageQuery query) {
        Long pageNo = normalizePageNo(query.getPageNo());
        Long pageSize = normalizePageSize(query.getPageSize());

        Page<MineMaintenanceTask> page = mineMaintenanceTaskMapper.selectPage(
                new Page<>(pageNo, pageSize),
                buildTaskWrapper(query)
        );
        List<MineMaintenanceTaskPageVO> records = page.getRecords().stream()
                .map(MineMaintenanceTaskPageVO::fromEntity)
                .toList();
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    @Override
    public MineMaintenanceTaskSummaryVO getSummary() {
        List<MineMaintenanceTask> tasks = mineMaintenanceTaskMapper.selectList(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getStatus, 0));

        MineMaintenanceTaskSummaryVO summary = new MineMaintenanceTaskSummaryVO();
        summary.setTaskTotal((long) tasks.size());
        summary.setPendingTotal(countTaskStatus(tasks, TASK_STATUS_PENDING));
        summary.setPlannedTotal(countTaskStatus(tasks, TASK_STATUS_PLANNED));
        summary.setProcessingTotal(countTaskStatus(tasks, TASK_STATUS_PROCESSING));
        summary.setClosedTotal(countTaskStatus(tasks, TASK_STATUS_CLOSED));
        summary.setHighRiskTaskTotal(tasks.stream()
                .filter(task -> Integer.valueOf(RISK_LEVEL_HIGH_RISK).equals(task.getRiskLevel()))
                .count());
        summary.setUrgentTotal(tasks.stream()
                .filter(task -> Integer.valueOf(3).equals(task.getPriority()))
                .count());
        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineMaintenanceTaskPageVO createFromDeviceHealth(MineMaintenanceTaskCreateRequest request) {
        MineDevice device = mineDeviceMapper.selectById(request.getDeviceId());
        if (device == null) {
            throw new BusinessException(500, "设备不存在，无法生成预测性维护任务");
        }

        DeviceHealthSnapshot health = calculateDeviceHealth(device);
        if (RISK_LEVEL_HEALTHY == health.riskLevel()) {
            throw new BusinessException(500, "当前设备风险等级为健康，暂不需要生成预测性维护任务");
        }

        MineMaintenanceTask existingTask = mineMaintenanceTaskMapper.selectOne(new LambdaQueryWrapper<MineMaintenanceTask>()
                .eq(MineMaintenanceTask::getDeviceId, device.getId())
                .in(MineMaintenanceTask::getTaskStatus, TASK_STATUS_PENDING, TASK_STATUS_PLANNED, TASK_STATUS_PROCESSING)
                .eq(MineMaintenanceTask::getStatus, 0)
                .orderByDesc(MineMaintenanceTask::getCreateTime)
                .last("LIMIT 1"));
        if (existingTask != null) {
            return MineMaintenanceTaskPageVO.fromEntity(existingTask);
        }

        LocalDateTime now = LocalDateTime.now();
        MineMaintenanceTask task = new MineMaintenanceTask();
        task.setTaskCode(generateTaskCode(now));
        task.setDeviceId(device.getId());
        task.setDeviceCode(device.getDeviceCode());
        task.setDeviceName(device.getDeviceName());
        task.setDeviceType(device.getDeviceType());
        task.setAreaName(device.getAreaName());
        task.setLocation(device.getLocation());
        task.setHealthScore(health.healthScore());
        task.setRiskLevel(health.riskLevel());
        task.setRiskLevelName(health.riskLevelName());
        task.setTaskTitle("预测性维护-" + device.getDeviceName() + "-" + health.riskLevelName());
        task.setTaskContent(buildTaskContent(health));
        task.setTaskType(1);
        task.setTaskSource(1);
        task.setTaskStatus(TASK_STATUS_PENDING);
        task.setPriority(toPriority(health.riskLevel()));
        task.setStatus(0);
        task.setCreateBy("system");
        task.setCreateTime(now);
        task.setUpdateBy("system");
        task.setUpdateTime(now);
        task.setDeleted(0);
        task.setRemark(request.getRemark());
        mineMaintenanceTaskMapper.insert(task);
        return MineMaintenanceTaskPageVO.fromEntity(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineMaintenanceTaskPageVO plan(Long id, MineMaintenanceTaskPlanRequest request) {
        MineMaintenanceTask task = getRequiredTask(id);
        ensureNotClosed(task);
        if (request.getPlanEndTime().isBefore(request.getPlanStartTime())) {
            throw new BusinessException(500, "计划结束时间不能早于计划开始时间");
        }

        LocalDateTime now = LocalDateTime.now();
        task.setPlanStartTime(request.getPlanStartTime());
        task.setPlanEndTime(request.getPlanEndTime());
        task.setMaintainerUserId(request.getMaintainerUserId());
        task.setMaintainerUsername(request.getMaintainerUsername());
        task.setTaskStatus(TASK_STATUS_PLANNED);
        task.setUpdateBy("system");
        task.setUpdateTime(now);
        if (StringUtils.hasText(request.getRemark())) {
            task.setRemark(request.getRemark());
        }
        mineMaintenanceTaskMapper.updateById(task);
        return MineMaintenanceTaskPageVO.fromEntity(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineMaintenanceTaskPageVO handle(Long id, MineMaintenanceTaskHandleRequest request) {
        MineMaintenanceTask task = getRequiredTask(id);
        ensureNotClosed(task);

        LocalDateTime now = LocalDateTime.now();
        task.setTaskStatus(TASK_STATUS_PROCESSING);
        task.setHandleTime(now);
        task.setHandleResult(request.getHandleResult());
        task.setUpdateBy("system");
        task.setUpdateTime(now);
        if (StringUtils.hasText(request.getRemark())) {
            task.setRemark(request.getRemark());
        }
        mineMaintenanceTaskMapper.updateById(task);
        return MineMaintenanceTaskPageVO.fromEntity(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineMaintenanceTaskPageVO close(Long id, MineMaintenanceTaskCloseRequest request) {
        MineMaintenanceTask task = getRequiredTask(id);
        ensureNotClosed(task);

        LocalDateTime now = LocalDateTime.now();
        task.setTaskStatus(TASK_STATUS_CLOSED);
        task.setCloseTime(now);
        task.setCloseResult(request.getCloseResult());
        task.setUpdateBy("system");
        task.setUpdateTime(now);
        if (StringUtils.hasText(request.getRemark())) {
            task.setRemark(request.getRemark());
        }
        mineMaintenanceTaskMapper.updateById(task);
        return MineMaintenanceTaskPageVO.fromEntity(task);
    }

    private LambdaQueryWrapper<MineMaintenanceTask> buildTaskWrapper(MineMaintenanceTaskPageQuery query) {
        LambdaQueryWrapper<MineMaintenanceTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getTaskCode()), MineMaintenanceTask::getTaskCode, query.getTaskCode())
                .like(StringUtils.hasText(query.getDeviceCode()), MineMaintenanceTask::getDeviceCode, query.getDeviceCode())
                .like(StringUtils.hasText(query.getDeviceName()), MineMaintenanceTask::getDeviceName, query.getDeviceName())
                .eq(StringUtils.hasText(query.getDeviceType()), MineMaintenanceTask::getDeviceType, query.getDeviceType())
                .like(StringUtils.hasText(query.getAreaName()), MineMaintenanceTask::getAreaName, query.getAreaName())
                .eq(query.getRiskLevel() != null, MineMaintenanceTask::getRiskLevel, query.getRiskLevel())
                .eq(query.getTaskStatus() != null, MineMaintenanceTask::getTaskStatus, query.getTaskStatus())
                .eq(query.getPriority() != null, MineMaintenanceTask::getPriority, query.getPriority())
                .eq(query.getStatus() != null, MineMaintenanceTask::getStatus, query.getStatus())
                .orderByDesc(MineMaintenanceTask::getCreateTime)
                .orderByDesc(MineMaintenanceTask::getId);
        return wrapper;
    }

    private MineMaintenanceTask getRequiredTask(Long id) {
        MineMaintenanceTask task = mineMaintenanceTaskMapper.selectById(id);
        if (task == null) {
            throw new BusinessException(500, "预测性维护任务不存在");
        }
        return task;
    }

    private void ensureNotClosed(MineMaintenanceTask task) {
        if (Integer.valueOf(TASK_STATUS_CLOSED).equals(task.getTaskStatus())) {
            throw new BusinessException(500, "预测性维护任务已关闭，不能重复操作");
        }
    }

    private DeviceHealthSnapshot calculateDeviceHealth(MineDevice device) {
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
        long offlineSensorCount = sensors.stream()
                .filter(sensor -> sensor.getStatus() == null
                        || sensor.getStatus() != 0
                        || sensor.getLastReportTime() == null
                        || sensor.getLastReportTime().isBefore(offlineTime))
                .count();

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

        int riskLevel = toRiskLevel(score);
        String riskLevelName = toRiskLevelName(score);
        return new DeviceHealthSnapshot(score, riskLevel, riskLevelName, alarmCount24h,
                severeUnhandledAlarmCount, unclosedWorkOrderCount, offlineSensorCount);
    }

    private String buildTaskContent(DeviceHealthSnapshot health) {
        return "设备当前健康分为 " + health.healthScore()
                + "，风险等级为 " + health.riskLevelName()
                + "。最近24小时告警数 " + health.alarmCount24h()
                + "，未处理严重告警数 " + health.severeUnhandledAlarmCount()
                + "，未关闭工单数 " + health.unclosedWorkOrderCount()
                + "，离线或异常传感器数 " + health.offlineSensorCount()
                + "。建议安排预测性维护，完成现场检查、备件确认和风险复核。";
    }

    private String generateTaskCode(LocalDateTime now) {
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "MT" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + random;
    }

    private Integer toPriority(Integer riskLevel) {
        if (RISK_LEVEL_HIGH_RISK == riskLevel) {
            return 3;
        }
        if (RISK_LEVEL_RISK == riskLevel) {
            return 2;
        }
        return 1;
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

    private Long countTaskStatus(List<MineMaintenanceTask> tasks, Integer taskStatus) {
        return tasks.stream()
                .filter(task -> taskStatus.equals(task.getTaskStatus()))
                .count();
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

    private record DeviceHealthSnapshot(Integer healthScore,
                                        Integer riskLevel,
                                        String riskLevelName,
                                        Long alarmCount24h,
                                        Long severeUnhandledAlarmCount,
                                        Long unclosedWorkOrderCount,
                                        Long offlineSensorCount) {
    }
}
