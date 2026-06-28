package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.dto.MineSensorDataSimulateRequest;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.entity.MineSensorData;
import cn.sxu.enterprise.module.mine.mapper.MineSensorDataMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.service.MineSensorDataService;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorDataVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
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
public class MineSensorDataServiceImpl implements MineSensorDataService {

    private final MineSensorDataMapper mineSensorDataMapper;

    private final MineSensorMapper mineSensorMapper;

    public MineSensorDataServiceImpl(MineSensorDataMapper mineSensorDataMapper,
                                     MineSensorMapper mineSensorMapper) {
        this.mineSensorDataMapper = mineSensorDataMapper;
        this.mineSensorMapper = mineSensorMapper;
    }

    @Override
    public List<MineSensorDataVO> simulate(MineSensorDataSimulateRequest request) {
        if (request == null) {
            request = new MineSensorDataSimulateRequest();
        }

        int count = request.getCount() == null ? 1 : request.getCount();
        if (count < 1) {
            count = 1;
        }
        if (count > 20) {
            count = 20;
        }

        LambdaQueryWrapper<MineSensor> sensorWrapper = new LambdaQueryWrapper<>();
        sensorWrapper.eq(MineSensor::getStatus, 0);

        if (request.getSensorId() != null) {
            sensorWrapper.eq(MineSensor::getId, request.getSensorId());
        }

        if (StringUtils.hasText(request.getSensorType())) {
            sensorWrapper.eq(MineSensor::getSensorType, request.getSensorType());
        }

        sensorWrapper.orderByAsc(MineSensor::getId);

        List<MineSensor> sensors = mineSensorMapper.selectList(sensorWrapper);
        List<MineSensorDataVO> result = new ArrayList<>();

        if (sensors == null || sensors.isEmpty()) {
            return result;
        }

        LocalDateTime now = LocalDateTime.now();

        for (MineSensor sensor : sensors) {
            for (int i = 0; i < count; i++) {
                MineSensorData data = buildSensorData(sensor, now.minusSeconds((long) count - i));
                mineSensorDataMapper.insert(data);
                result.add(MineSensorDataVO.fromEntity(data));
            }

            MineSensor updateSensor = new MineSensor();
            updateSensor.setId(sensor.getId());
            updateSensor.setLastReportTime(now);
            mineSensorMapper.updateById(updateSensor);
        }

        return result;
    }

    @Override
    public List<MineSensorDataVO> latest(MineSensorDataPageQuery query) {
        if (query == null) {
            query = new MineSensorDataPageQuery();
        }

        LambdaQueryWrapper<MineSensorData> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(MineSensorData::getCollectTime);
        wrapper.orderByDesc(MineSensorData::getId);

        List<MineSensorData> list = mineSensorDataMapper.selectList(wrapper);

        Map<Long, MineSensorData> latestMap = new LinkedHashMap<>();
        for (MineSensorData data : list) {
            if (data.getSensorId() != null && !latestMap.containsKey(data.getSensorId())) {
                latestMap.put(data.getSensorId(), data);
            }
        }

        return latestMap.values()
                .stream()
                .map(MineSensorDataVO::fromEntity)
                .toList();
    }

    @Override
    public PageResult<MineSensorDataVO> pageSensorData(MineSensorDataPageQuery query) {
        if (query == null) {
            query = new MineSensorDataPageQuery();
        }

        Long pageNo = query.getPageNo() == null ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null ? 10L : query.getPageSize();

        Page<MineSensorData> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<MineSensorData> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(MineSensorData::getCollectTime);
        wrapper.orderByDesc(MineSensorData::getId);

        Page<MineSensorData> resultPage = mineSensorDataMapper.selectPage(page, wrapper);

        List<MineSensorDataVO> records = resultPage.getRecords()
                .stream()
                .map(MineSensorDataVO::fromEntity)
                .toList();

        return PageResult.of(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                records
        );
    }

    private LambdaQueryWrapper<MineSensorData> buildQueryWrapper(MineSensorDataPageQuery query) {
        LambdaQueryWrapper<MineSensorData> wrapper = new LambdaQueryWrapper<>();

        if (query.getSensorId() != null) {
            wrapper.eq(MineSensorData::getSensorId, query.getSensorId());
        }

        if (StringUtils.hasText(query.getSensorCode())) {
            wrapper.like(MineSensorData::getSensorCode, query.getSensorCode());
        }

        if (StringUtils.hasText(query.getSensorName())) {
            wrapper.like(MineSensorData::getSensorName, query.getSensorName());
        }

        if (StringUtils.hasText(query.getSensorType())) {
            wrapper.eq(MineSensorData::getSensorType, query.getSensorType());
        }

        if (StringUtils.hasText(query.getAreaName())) {
            wrapper.like(MineSensorData::getAreaName, query.getAreaName());
        }

        if (query.getAlarmFlag() != null) {
            wrapper.eq(MineSensorData::getAlarmFlag, query.getAlarmFlag());
        }

        if (query.getStatus() != null) {
            wrapper.eq(MineSensorData::getStatus, query.getStatus());
        }

        return wrapper;
    }

    private MineSensorData buildSensorData(MineSensor sensor, LocalDateTime collectTime) {
        BigDecimal dataValue = randomValue(sensor);
        BigDecimal threshold = sensor.getAlarmThreshold();

        int alarmFlag = 0;
        if (threshold != null && dataValue.compareTo(threshold) >= 0) {
            alarmFlag = 1;
        }

        int status = alarmFlag == 1 ? 1 : 0;

        return new MineSensorData()
                .setSensorId(sensor.getId())
                .setSensorCode(sensor.getSensorCode())
                .setSensorName(sensor.getSensorName())
                .setSensorType(sensor.getSensorType())
                .setDeviceId(sensor.getDeviceId())
                .setAreaName(sensor.getAreaName())
                .setLocation(sensor.getLocation())
                .setDataValue(dataValue)
                .setUnit(sensor.getUnit())
                .setAlarmThreshold(threshold)
                .setAlarmFlag(alarmFlag)
                .setCollectTime(collectTime)
                .setStatus(status)
                .setCreateBy("system")
                .setCreateTime(LocalDateTime.now())
                .setDeleted(0)
                .setRemark("M1-03 模拟生成传感器数据");
    }

    private BigDecimal randomValue(MineSensor sensor) {
        BigDecimal minValue = sensor.getMinValue();
        BigDecimal maxValue = sensor.getMaxValue();

        if (minValue == null || maxValue == null || maxValue.compareTo(minValue) <= 0) {
            String sensorType = sensor.getSensorType();

            if ("GAS".equals(sensorType)) {
                minValue = BigDecimal.valueOf(0.10);
                maxValue = BigDecimal.valueOf(1.50);
            } else if ("TEMPERATURE".equals(sensorType)) {
                minValue = BigDecimal.valueOf(18.00);
                maxValue = BigDecimal.valueOf(45.00);
            } else if ("VIBRATION".equals(sensorType)) {
                minValue = BigDecimal.valueOf(0.50);
                maxValue = BigDecimal.valueOf(12.00);
            } else {
                minValue = BigDecimal.ZERO;
                maxValue = BigDecimal.valueOf(100.00);
            }
        }

        double min = minValue.doubleValue();
        double max = maxValue.doubleValue();

        double value = ThreadLocalRandom.current().nextDouble(min, max);

        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }
}