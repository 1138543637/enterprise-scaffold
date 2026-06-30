package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.dto.AiopsMetricDataSimulateRequest;
import cn.sxu.enterprise.module.aiops.entity.AiopsMetricData;
import cn.sxu.enterprise.module.aiops.entity.AiopsResource;
import cn.sxu.enterprise.module.aiops.mapper.AiopsMetricDataMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsResourceMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsMetricDataService;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsMetricDataVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AiopsMetricDataServiceImpl implements AiopsMetricDataService {

    private final AiopsMetricDataMapper aiopsMetricDataMapper;

    private final AiopsResourceMapper aiopsResourceMapper;

    public AiopsMetricDataServiceImpl(AiopsMetricDataMapper aiopsMetricDataMapper,
                                      AiopsResourceMapper aiopsResourceMapper) {
        this.aiopsMetricDataMapper = aiopsMetricDataMapper;
        this.aiopsResourceMapper = aiopsResourceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AiopsMetricDataVO> simulate(AiopsMetricDataSimulateRequest request) {
        AiopsMetricDataSimulateRequest safeRequest = request == null ? new AiopsMetricDataSimulateRequest() : request;

        int count = safeRequest.getCount() == null ? 1 : safeRequest.getCount();
        if (count < 1 || count > 20) {
            throw new BusinessException(500, "每个资源生成指标数据的轮数必须在 1 到 20 之间");
        }

        List<AiopsResource> resources = selectEnabledResources(safeRequest);
        if (resources.isEmpty()) {
            throw new BusinessException(500, "未找到启用采集的 AIOps 资源，请先检查 aiops_resource 数据");
        }

        List<AiopsMetricDataVO> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (AiopsResource resource : resources) {
            List<MetricDefinition> metricDefinitions = buildMetricDefinitions(resource.getResourceType(), safeRequest.getMetricType());

            if (metricDefinitions.isEmpty()) {
                continue;
            }

            LocalDateTime lastCollectTime = now;

            for (int i = 0; i < count; i++) {
                LocalDateTime collectTime = now.minusSeconds((long) (count - i - 1) * 10);

                for (MetricDefinition definition : metricDefinitions) {
                    AiopsMetricData data = buildMetricData(resource, definition, collectTime, safeRequest.getRemark());
                    aiopsMetricDataMapper.insert(data);
                    result.add(AiopsMetricDataVO.fromEntity(data));
                    lastCollectTime = collectTime;
                }
            }

            resource.setLastCollectTime(lastCollectTime);
            resource.setUpdateTime(LocalDateTime.now());
            aiopsResourceMapper.updateById(resource);
        }

        if (result.isEmpty()) {
            throw new BusinessException(500, "未生成任何指标数据，请检查 resourceType 或 metricType 是否填写正确");
        }

        return result;
    }

    @Override
    public List<AiopsMetricDataVO> latest(AiopsMetricDataPageQuery query) {
        LambdaQueryWrapper<AiopsMetricData> wrapper = buildQueryWrapper(query)
                .orderByDesc(AiopsMetricData::getCollectTime)
                .orderByDesc(AiopsMetricData::getId)
                .last("LIMIT 500");

        List<AiopsMetricData> list = aiopsMetricDataMapper.selectList(wrapper);

        Map<String, AiopsMetricData> latestMap = new LinkedHashMap<>();
        for (AiopsMetricData data : list) {
            String key = data.getResourceCode() + "::" + data.getMetricCode();
            latestMap.putIfAbsent(key, data);
        }

        return latestMap.values()
                .stream()
                .map(AiopsMetricDataVO::fromEntity)
                .toList();
    }

    @Override
    public PageResult<AiopsMetricDataVO> pageMetricData(AiopsMetricDataPageQuery query) {
        Long pageNo = query.getPageNo() == null ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null ? 10L : query.getPageSize();

        Page<AiopsMetricData> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<AiopsMetricData> wrapper = buildQueryWrapper(query)
                .orderByDesc(AiopsMetricData::getCollectTime)
                .orderByDesc(AiopsMetricData::getId);

        Page<AiopsMetricData> result = aiopsMetricDataMapper.selectPage(page, wrapper);

        List<AiopsMetricDataVO> records = result.getRecords()
                .stream()
                .map(AiopsMetricDataVO::fromEntity)
                .toList();

        return PageResult.of(
                result.getCurrent(),
                result.getSize(),
                result.getTotal(),
                result.getPages(),
                records
        );
    }

    private List<AiopsResource> selectEnabledResources(AiopsMetricDataSimulateRequest request) {
        LambdaQueryWrapper<AiopsResource> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(AiopsResource::getCollectEnabled, 0);
        wrapper.eq(AiopsResource::getStatus, 0);

        if (request.getResourceId() != null) {
            wrapper.eq(AiopsResource::getId, request.getResourceId());
        }

        if (StringUtils.hasText(request.getResourceType())) {
            wrapper.eq(AiopsResource::getResourceType, request.getResourceType());
        }

        wrapper.orderByAsc(AiopsResource::getId);

        return aiopsResourceMapper.selectList(wrapper);
    }

    private LambdaQueryWrapper<AiopsMetricData> buildQueryWrapper(AiopsMetricDataPageQuery query) {
        AiopsMetricDataPageQuery safeQuery = query == null ? new AiopsMetricDataPageQuery() : query;

        LambdaQueryWrapper<AiopsMetricData> wrapper = new LambdaQueryWrapper<>();

        if (safeQuery.getResourceId() != null) {
            wrapper.eq(AiopsMetricData::getResourceId, safeQuery.getResourceId());
        }

        if (StringUtils.hasText(safeQuery.getResourceCode())) {
            wrapper.like(AiopsMetricData::getResourceCode, safeQuery.getResourceCode());
        }

        if (StringUtils.hasText(safeQuery.getResourceName())) {
            wrapper.like(AiopsMetricData::getResourceName, safeQuery.getResourceName());
        }

        if (StringUtils.hasText(safeQuery.getResourceType())) {
            wrapper.eq(AiopsMetricData::getResourceType, safeQuery.getResourceType());
        }

        if (StringUtils.hasText(safeQuery.getIpAddr())) {
            wrapper.like(AiopsMetricData::getIpAddr, safeQuery.getIpAddr());
        }

        if (StringUtils.hasText(safeQuery.getMetricCode())) {
            wrapper.like(AiopsMetricData::getMetricCode, safeQuery.getMetricCode());
        }

        if (StringUtils.hasText(safeQuery.getMetricType())) {
            wrapper.eq(AiopsMetricData::getMetricType, safeQuery.getMetricType());
        }

        if (safeQuery.getAlarmFlag() != null) {
            wrapper.eq(AiopsMetricData::getAlarmFlag, safeQuery.getAlarmFlag());
        }

        if (safeQuery.getStatus() != null) {
            wrapper.eq(AiopsMetricData::getStatus, safeQuery.getStatus());
        }

        return wrapper;
    }

    private AiopsMetricData buildMetricData(AiopsResource resource,
                                            MetricDefinition definition,
                                            LocalDateTime collectTime,
                                            String requestRemark) {
        BigDecimal metricValue = randomValue(definition);
        Integer alarmFlag = metricValue.compareTo(definition.thresholdValue()) >= 0 ? 1 : 0;

        String remark = StringUtils.hasText(requestRemark)
                ? requestRemark
                : "A2-03 指标模拟数据";

        return new AiopsMetricData()
                .setResourceId(resource.getId())
                .setResourceCode(resource.getResourceCode())
                .setResourceName(resource.getResourceName())
                .setResourceType(resource.getResourceType())
                .setIpAddr(resource.getIpAddr())
                .setMetricCode(definition.metricCode())
                .setMetricName(definition.metricName())
                .setMetricType(definition.metricType())
                .setMetricValue(metricValue)
                .setMetricUnit(definition.metricUnit())
                .setThresholdValue(definition.thresholdValue())
                .setAlarmFlag(alarmFlag)
                .setCollectTime(collectTime)
                .setStatus(alarmFlag)
                .setCreateBy("system")
                .setCreateTime(LocalDateTime.now())
                .setDeleted(0)
                .setRemark(remark);
    }

    private BigDecimal randomValue(MetricDefinition definition) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        double value = random.nextDouble(definition.minValue(), definition.maxValue());

        if (random.nextInt(100) < 18) {
            double threshold = definition.thresholdValue().doubleValue();
            value = threshold * random.nextDouble(1.01, 1.25);
        }

        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    private List<MetricDefinition> buildMetricDefinitions(String resourceType, String metricTypeFilter) {
        List<MetricDefinition> definitions = switch (resourceType) {
            case "SERVER" -> List.of(
                    new MetricDefinition("CPU_USAGE", "CPU使用率", "CPU", "%", 5, 95, bd("80")),
                    new MetricDefinition("MEMORY_USAGE", "内存使用率", "MEMORY", "%", 10, 98, bd("85")),
                    new MetricDefinition("DISK_USAGE", "磁盘使用率", "DISK", "%", 15, 99, bd("90")),
                    new MetricDefinition("NETWORK_IN", "入站网络流量", "NETWORK", "Mbps", 20, 900, bd("700"))
            );
            case "DATABASE" -> List.of(
                    new MetricDefinition("MYSQL_CPU_USAGE", "数据库CPU使用率", "CPU", "%", 5, 95, bd("80")),
                    new MetricDefinition("MYSQL_CONNECTIONS", "MySQL连接数", "MYSQL", "count", 20, 1200, bd("800")),
                    new MetricDefinition("MYSQL_SLOW_SQL", "MySQL慢SQL数量", "MYSQL", "count", 0, 40, bd("20")),
                    new MetricDefinition("MYSQL_QPS", "MySQL每秒查询数", "MYSQL", "qps", 50, 3500, bd("2500"))
            );
            case "MIDDLEWARE" -> List.of(
                    new MetricDefinition("REDIS_MEMORY_USAGE", "Redis内存使用率", "REDIS", "%", 10, 98, bd("85")),
                    new MetricDefinition("REDIS_QPS", "Redis每秒请求数", "REDIS", "qps", 50, 5000, bd("3500")),
                    new MetricDefinition("REDIS_CONNECTIONS", "Redis连接数", "REDIS", "count", 10, 1500, bd("1000"))
            );
            case "NETWORK" -> List.of(
                    new MetricDefinition("NETWORK_LATENCY", "网络延迟", "NETWORK", "ms", 1, 180, bd("100")),
                    new MetricDefinition("PACKET_LOSS", "丢包率", "NETWORK", "%", 0, 12, bd("5")),
                    new MetricDefinition("NETWORK_IN", "入站网络流量", "NETWORK", "Mbps", 20, 900, bd("700"))
            );
            default -> List.of(
                    new MetricDefinition("CPU_USAGE", "CPU使用率", "CPU", "%", 5, 95, bd("80")),
                    new MetricDefinition("MEMORY_USAGE", "内存使用率", "MEMORY", "%", 10, 98, bd("85"))
            );
        };

        if (!StringUtils.hasText(metricTypeFilter)) {
            return definitions;
        }

        return definitions.stream()
                .filter(item -> metricTypeFilter.equals(item.metricType()))
                .toList();
    }

    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
    }

    private record MetricDefinition(
            String metricCode,
            String metricName,
            String metricType,
            String metricUnit,
            double minValue,
            double maxValue,
            BigDecimal thresholdValue
    ) {
    }
}
