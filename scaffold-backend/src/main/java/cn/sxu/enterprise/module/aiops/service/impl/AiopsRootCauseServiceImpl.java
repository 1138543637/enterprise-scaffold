package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsRootCauseAnalyzeRequest;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertEvent;
import cn.sxu.enterprise.module.aiops.entity.AiopsMetricData;
import cn.sxu.enterprise.module.aiops.entity.AiopsResource;
import cn.sxu.enterprise.module.aiops.entity.AiopsRootCauseRecord;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertEventMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsMetricDataMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsResourceMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsRootCauseRecordMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsWorkOrderMapper;
import cn.sxu.enterprise.module.aiops.entity.AiopsWorkOrder;
import cn.sxu.enterprise.module.aiops.service.AiopsRootCauseService;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCausePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsRootCauseVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AiopsRootCauseServiceImpl implements AiopsRootCauseService {

    private static final int DEFAULT_LOOKBACK_MINUTES = 30;

    private final AiopsRootCauseRecordMapper rootCauseRecordMapper;

    private final AiopsAlertEventMapper alertEventMapper;

    private final AiopsMetricDataMapper metricDataMapper;

    private final AiopsWorkOrderMapper workOrderMapper;

    private final AiopsResourceMapper resourceMapper;

    public AiopsRootCauseServiceImpl(AiopsRootCauseRecordMapper rootCauseRecordMapper,
                                     AiopsAlertEventMapper alertEventMapper,
                                     AiopsMetricDataMapper metricDataMapper,
                                     AiopsWorkOrderMapper workOrderMapper,
                                     AiopsResourceMapper resourceMapper) {
        this.rootCauseRecordMapper = rootCauseRecordMapper;
        this.alertEventMapper = alertEventMapper;
        this.metricDataMapper = metricDataMapper;
        this.workOrderMapper = workOrderMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiopsRootCauseVO analyze(AiopsRootCauseAnalyzeRequest request) {
        AiopsAlertEvent alertEvent = alertEventMapper.selectById(request.getAlertEventId());
        if (alertEvent == null) {
            throw new BusinessException(500, "告警事件不存在，无法执行根因分析");
        }

        AiopsRootCauseRecord existed = rootCauseRecordMapper.selectOne(
                new LambdaQueryWrapper<AiopsRootCauseRecord>()
                        .eq(AiopsRootCauseRecord::getAlertEventId, request.getAlertEventId())
                        .eq(AiopsRootCauseRecord::getStatus, 0)
                        .last("LIMIT 1")
        );
        if (existed != null) {
            return AiopsRootCauseVO.fromEntity(existed);
        }

        int lookbackMinutes = request.getLookbackMinutes() == null
                ? DEFAULT_LOOKBACK_MINUTES
                : request.getLookbackMinutes();

        LocalDateTime endTime = alertEvent.getAlertTime() == null
                ? LocalDateTime.now()
                : alertEvent.getAlertTime();
        LocalDateTime startTime = endTime.minusMinutes(lookbackMinutes);

        List<AiopsMetricData> recentMetrics = queryRecentMetrics(alertEvent, startTime, endTime);
        long abnormalMetricCount = recentMetrics.stream()
                .filter(this::isAbnormalMetric)
                .count();

        long sameResourceAlertCount = alertEventMapper.selectCount(
                new LambdaQueryWrapper<AiopsAlertEvent>()
                        .eq(alertEvent.getResourceId() != null, AiopsAlertEvent::getResourceId, alertEvent.getResourceId())
                        .eq(alertEvent.getResourceId() == null && StringUtils.hasText(alertEvent.getResourceCode()),
                                AiopsAlertEvent::getResourceCode, alertEvent.getResourceCode())
                        .between(AiopsAlertEvent::getAlertTime, startTime, endTime)
                        .ne(alertEvent.getId() != null, AiopsAlertEvent::getId, alertEvent.getId())
                        .eq(AiopsAlertEvent::getStatus, 0)
        );

        long sameSystemAlertCount = countSameSystemAlerts(alertEvent, startTime, endTime);

        long unclosedWorkOrderCount = workOrderMapper.selectCount(
                new LambdaQueryWrapper<AiopsWorkOrder>()
                        .eq(alertEvent.getResourceId() != null, AiopsWorkOrder::getResourceId, alertEvent.getResourceId())
                        .eq(alertEvent.getResourceId() == null && StringUtils.hasText(alertEvent.getResourceCode()),
                                AiopsWorkOrder::getResourceCode, alertEvent.getResourceCode())
                        .ne(AiopsWorkOrder::getOrderStatus, 3)
                        .eq(AiopsWorkOrder::getStatus, 0)
        );

        RootCauseDecision decision = decideRootCause(
                alertEvent,
                abnormalMetricCount,
                sameResourceAlertCount,
                sameSystemAlertCount,
                unclosedWorkOrderCount,
                lookbackMinutes
        );

        AiopsRootCauseRecord record = new AiopsRootCauseRecord();
        record.setAnalysisCode(generateAnalysisCode());
        record.setAlertEventId(alertEvent.getId());
        record.setEventCode(alertEvent.getEventCode());
        record.setResourceId(alertEvent.getResourceId());
        record.setResourceCode(alertEvent.getResourceCode());
        record.setResourceName(alertEvent.getResourceName());
        record.setResourceType(alertEvent.getResourceType());
        record.setIpAddr(alertEvent.getIpAddr());
        record.setRootCauseType(decision.rootCauseType());
        record.setRootCauseDesc(decision.rootCauseDesc());
        record.setEvidence(decision.evidence());
        record.setSuggestion(decision.suggestion());
        record.setConfidenceScore(decision.confidenceScore());
        record.setAnalysisTime(LocalDateTime.now());
        record.setStatus(0);
        record.setCreateBy("system");
        record.setCreateTime(LocalDateTime.now());
        record.setDeleted(0);
        record.setRemark(request.getRemark());

        rootCauseRecordMapper.insert(record);
        return AiopsRootCauseVO.fromEntity(record);
    }

    @Override
    public PageResult<AiopsRootCauseVO> pageRootCauses(AiopsRootCausePageQuery query) {
        Long pageNo = query.getPageNo() == null ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null ? 10L : query.getPageSize();

        LambdaQueryWrapper<AiopsRootCauseRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getAnalysisCode()),
                AiopsRootCauseRecord::getAnalysisCode, query.getAnalysisCode());
        wrapper.like(StringUtils.hasText(query.getEventCode()),
                AiopsRootCauseRecord::getEventCode, query.getEventCode());
        wrapper.like(StringUtils.hasText(query.getResourceCode()),
                AiopsRootCauseRecord::getResourceCode, query.getResourceCode());
        wrapper.like(StringUtils.hasText(query.getResourceName()),
                AiopsRootCauseRecord::getResourceName, query.getResourceName());
        wrapper.eq(StringUtils.hasText(query.getResourceType()),
                AiopsRootCauseRecord::getResourceType, query.getResourceType());
        wrapper.like(StringUtils.hasText(query.getIpAddr()),
                AiopsRootCauseRecord::getIpAddr, query.getIpAddr());
        wrapper.eq(StringUtils.hasText(query.getRootCauseType()),
                AiopsRootCauseRecord::getRootCauseType, query.getRootCauseType());
        wrapper.eq(query.getStatus() != null,
                AiopsRootCauseRecord::getStatus, query.getStatus());
        wrapper.orderByDesc(AiopsRootCauseRecord::getAnalysisTime)
                .orderByDesc(AiopsRootCauseRecord::getId);

        Page<AiopsRootCauseRecord> page = rootCauseRecordMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<AiopsRootCauseVO> records = page.getRecords()
                .stream()
                .map(AiopsRootCauseVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(pageNo, pageSize, page.getTotal(), page.getPages(), records);
    }

    @Override
    public AiopsRootCauseVO detail(Long id) {
        AiopsRootCauseRecord record = rootCauseRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(500, "根因分析记录不存在");
        }
        return AiopsRootCauseVO.fromEntity(record);
    }

    private List<AiopsMetricData> queryRecentMetrics(AiopsAlertEvent alertEvent,
                                                     LocalDateTime startTime,
                                                     LocalDateTime endTime) {
        LambdaQueryWrapper<AiopsMetricData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(alertEvent.getResourceId() != null, AiopsMetricData::getResourceId, alertEvent.getResourceId());
        wrapper.eq(alertEvent.getResourceId() == null && StringUtils.hasText(alertEvent.getResourceCode()),
                AiopsMetricData::getResourceCode, alertEvent.getResourceCode());
        wrapper.between(AiopsMetricData::getCollectTime, startTime, endTime);
        wrapper.orderByDesc(AiopsMetricData::getCollectTime)
                .orderByDesc(AiopsMetricData::getId);
        wrapper.last("LIMIT 50");
        return metricDataMapper.selectList(wrapper);
    }

    private boolean isAbnormalMetric(AiopsMetricData metricData) {
        return Integer.valueOf(1).equals(metricData.getAlarmFlag())
                || Integer.valueOf(1).equals(metricData.getStatus());
    }

    private long countSameSystemAlerts(AiopsAlertEvent alertEvent,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime) {
        if (alertEvent.getResourceId() == null) {
            return 0L;
        }

        AiopsResource currentResource = resourceMapper.selectById(alertEvent.getResourceId());
        if (currentResource == null || !StringUtils.hasText(currentResource.getSystemName())) {
            return 0L;
        }

        List<AiopsResource> sameSystemResources = resourceMapper.selectList(
                new LambdaQueryWrapper<AiopsResource>()
                        .eq(AiopsResource::getSystemName, currentResource.getSystemName())
                        .eq(AiopsResource::getStatus, 0)
        );
        List<Long> resourceIds = sameSystemResources.stream()
                .map(AiopsResource::getId)
                .collect(Collectors.toList());
        if (resourceIds.isEmpty()) {
            return 0L;
        }

        return alertEventMapper.selectCount(
                new LambdaQueryWrapper<AiopsAlertEvent>()
                        .in(AiopsAlertEvent::getResourceId, resourceIds)
                        .between(AiopsAlertEvent::getAlertTime, startTime, endTime)
                        .eq(AiopsAlertEvent::getStatus, 0)
        );
    }

    private RootCauseDecision decideRootCause(AiopsAlertEvent alertEvent,
                                              long abnormalMetricCount,
                                              long sameResourceAlertCount,
                                              long sameSystemAlertCount,
                                              long unclosedWorkOrderCount,
                                              int lookbackMinutes) {
        String metricType = alertEvent.getMetricType();
        String rootCauseType = mapRootCauseType(metricType);
        int confidenceScore = calculateConfidenceScore(
                rootCauseType,
                abnormalMetricCount,
                sameResourceAlertCount,
                sameSystemAlertCount,
                unclosedWorkOrderCount
        );

        List<String> evidenceList = new ArrayList<>();
        evidenceList.add("告警事件：" + safe(alertEvent.getEventCode()));
        evidenceList.add("告警资源：" + safe(alertEvent.getResourceName()) + "（" + safe(alertEvent.getResourceCode()) + "，" + safe(alertEvent.getIpAddr()) + "）");
        evidenceList.add("告警指标：" + safe(alertEvent.getMetricName()) + "（" + safe(alertEvent.getMetricType()) + "）");
        evidenceList.add("指标值：" + formatBigDecimal(alertEvent.getMetricValue()) + "，阈值：" + formatBigDecimal(alertEvent.getThresholdValue()));
        evidenceList.add("回看窗口：" + lookbackMinutes + " 分钟");
        evidenceList.add("窗口内异常指标数量：" + abnormalMetricCount);
        evidenceList.add("窗口内同资源其他告警数量：" + sameResourceAlertCount);
        evidenceList.add("窗口内同系统告警数量：" + sameSystemAlertCount);
        evidenceList.add("当前未关闭运维工单数量：" + unclosedWorkOrderCount);

        String rootCauseDesc = buildRootCauseDesc(rootCauseType, alertEvent);
        String suggestion = buildSuggestion(rootCauseType);

        if ("UNKNOWN".equals(rootCauseType) && abnormalMetricCount == 0 && sameResourceAlertCount == 0 && sameSystemAlertCount == 0) {
            evidenceList.add("未发现明显同类指标异常或同资源告警，需要人工结合日志、链路和主机状态继续排查。");
        }

        return new RootCauseDecision(
                rootCauseType,
                rootCauseDesc,
                String.join("\n", evidenceList),
                suggestion,
                confidenceScore
        );
    }

    private String mapRootCauseType(String metricType) {
        if ("CPU".equals(metricType)) {
            return "CPU_HIGH";
        }
        if ("MEMORY".equals(metricType)) {
            return "MEMORY_HIGH";
        }
        if ("DISK".equals(metricType)) {
            return "DISK_FULL";
        }
        if ("NETWORK".equals(metricType)) {
            return "NETWORK_ABNORMAL";
        }
        if ("MYSQL".equals(metricType)) {
            return "DATABASE_SLOW";
        }
        return "UNKNOWN";
    }

    private int calculateConfidenceScore(String rootCauseType,
                                         long abnormalMetricCount,
                                         long sameResourceAlertCount,
                                         long sameSystemAlertCount,
                                         long unclosedWorkOrderCount) {
        int score;
        if (!"UNKNOWN".equals(rootCauseType)) {
            score = 75;
        } else {
            score = 50;
        }

        score += Math.min(10, (int) abnormalMetricCount * 2);
        score += Math.min(8, (int) sameResourceAlertCount * 2);
        score += Math.min(5, (int) sameSystemAlertCount);
        if (unclosedWorkOrderCount > 0) {
            score += 5;
        }

        return Math.max(0, Math.min(100, score));
    }

    private String buildRootCauseDesc(String rootCauseType, AiopsAlertEvent alertEvent) {
        if ("CPU_HIGH".equals(rootCauseType)) {
            return "疑似 CPU 使用率过高，可能由突发流量、异常进程或计算任务堆积导致。";
        }
        if ("MEMORY_HIGH".equals(rootCauseType)) {
            return "疑似内存使用率过高，可能由内存泄漏、缓存膨胀或应用实例负载过高导致。";
        }
        if ("DISK_FULL".equals(rootCauseType)) {
            return "疑似磁盘空间不足，可能由日志增长、临时文件堆积或数据文件膨胀导致。";
        }
        if ("NETWORK_ABNORMAL".equals(rootCauseType)) {
            return "疑似网络流量或网络质量异常，可能由链路拥塞、端口错误或网络设备异常导致。";
        }
        if ("DATABASE_SLOW".equals(rootCauseType)) {
            return "疑似数据库响应变慢，可能由慢 SQL、连接数过高、锁等待或磁盘 IO 压力导致。";
        }
        return "暂未匹配到明确根因类型，需要结合指标、日志、工单和人工经验继续排查。告警指标为：" + safe(alertEvent.getMetricType());
    }

    private String buildSuggestion(String rootCauseType) {
        if ("CPU_HIGH".equals(rootCauseType)) {
            return "建议先查看服务器 CPU Top 进程、应用线程池和最近发布记录；必要时扩容实例或限制异常任务。";
        }
        if ("MEMORY_HIGH".equals(rootCauseType)) {
            return "建议检查 JVM 堆内存、缓存对象、内存泄漏和进程 RSS；必要时重启异常实例并保留 dump 证据。";
        }
        if ("DISK_FULL".equals(rootCauseType)) {
            return "建议清理过期日志和临时文件，检查磁盘增长目录，必要时扩容磁盘并增加日志轮转策略。";
        }
        if ("NETWORK_ABNORMAL".equals(rootCauseType)) {
            return "建议检查交换机端口、链路带宽、丢包率和网络设备告警；必要时切换链路或联系网络管理员。";
        }
        if ("DATABASE_SLOW".equals(rootCauseType)) {
            return "建议检查慢 SQL、数据库连接池、锁等待、主从延迟和磁盘 IO；必要时优化索引或扩容数据库资源。";
        }
        return "建议先查看同时间窗口内指标趋势、应用日志、系统日志和未关闭工单，再由运维人员确认最终根因。";
    }

    private String generateAnalysisCode() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "AIOPS-RCA-" + timePart + "-" + randomPart;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String formatBigDecimal(BigDecimal value) {
        return value == null ? "" : value.stripTrailingZeros().toPlainString();
    }

    private record RootCauseDecision(String rootCauseType,
                                     String rootCauseDesc,
                                     String evidence,
                                     String suggestion,
                                     Integer confidenceScore) {
    }
}
