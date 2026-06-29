package cn.sxu.enterprise.module.mine.listener;

import cn.sxu.enterprise.module.mine.dto.MineAlarmGenerateRequest;
import cn.sxu.enterprise.module.mine.dto.MineSensorMqttMessage;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.entity.MineSensorData;
import cn.sxu.enterprise.module.mine.mapper.MineSensorDataMapper;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.service.MineAlarmEventService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(prefix = "enterprise.mine.mqtt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MineSensorMqttListener {

    private static final Logger log = LoggerFactory.getLogger(MineSensorMqttListener.class);

    private final ObjectMapper objectMapper;

    private final MineSensorMapper mineSensorMapper;

    private final MineSensorDataMapper mineSensorDataMapper;

    private final MineAlarmEventService mineAlarmEventService;

    public MineSensorMqttListener(ObjectMapper objectMapper,
                                  MineSensorMapper mineSensorMapper,
                                  MineSensorDataMapper mineSensorDataMapper,
                                  MineAlarmEventService mineAlarmEventService) {
        this.objectMapper = objectMapper;
        this.mineSensorMapper = mineSensorMapper;
        this.mineSensorDataMapper = mineSensorDataMapper;
        this.mineAlarmEventService = mineAlarmEventService;
    }

    @ServiceActivator(inputChannel = "mineMqttInputChannel")
    public void handleMessage(Message<?> message) {
        String payload = resolvePayload(message.getPayload());
        try {
            MineSensorMqttMessage mqttMessage = objectMapper.readValue(payload, MineSensorMqttMessage.class);
            handleSensorMessage(mqttMessage);
        } catch (Exception e) {
            log.error("MQTT sensor message handle failed, payload={}", payload, e);
        }
    }

    private void handleSensorMessage(MineSensorMqttMessage mqttMessage) {
        if (!StringUtils.hasText(mqttMessage.getSensorCode())) {
            log.warn("MQTT sensor message ignored, sensorCode is blank");
            return;
        }
        if (mqttMessage.getDataValue() == null) {
            log.warn("MQTT sensor message ignored, dataValue is null, sensorCode={}", mqttMessage.getSensorCode());
            return;
        }

        MineSensor sensor = mineSensorMapper.selectOne(new LambdaQueryWrapper<MineSensor>()
                .eq(MineSensor::getSensorCode, mqttMessage.getSensorCode())
                .eq(MineSensor::getStatus, 0)
                .last("limit 1"));

        if (sensor == null) {
            log.warn("MQTT sensor message ignored, active sensor not found, sensorCode={}", mqttMessage.getSensorCode());
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime collectTime = mqttMessage.getCollectTime() == null ? now : mqttMessage.getCollectTime();

        MineSensorData sensorData = buildSensorData(sensor, mqttMessage, collectTime, now);
        mineSensorDataMapper.insert(sensorData);

        sensor.setLastReportTime(collectTime);
        sensor.setUpdateBy("mqtt");
        sensor.setUpdateTime(now);
        mineSensorMapper.updateById(sensor);

        generateAlarm(sensorData);
        log.info("MQTT sensor message saved, sensorCode={}, sensorDataId={}", sensorData.getSensorCode(), sensorData.getId());
    }

    private MineSensorData buildSensorData(MineSensor sensor,
                                           MineSensorMqttMessage mqttMessage,
                                           LocalDateTime collectTime,
                                           LocalDateTime now) {
        BigDecimal dataValue = mqttMessage.getDataValue();
        BigDecimal alarmThreshold = sensor.getAlarmThreshold();
        int alarmFlag = isAlarm(dataValue, alarmThreshold) ? 1 : 0;

        MineSensorData sensorData = new MineSensorData();
        sensorData.setSensorId(sensor.getId());
        sensorData.setSensorCode(sensor.getSensorCode());
        sensorData.setSensorName(sensor.getSensorName());
        sensorData.setSensorType(sensor.getSensorType());
        sensorData.setDeviceId(sensor.getDeviceId());
        sensorData.setAreaName(sensor.getAreaName());
        sensorData.setLocation(sensor.getLocation());
        sensorData.setDataValue(dataValue);
        sensorData.setUnit(sensor.getUnit());
        sensorData.setAlarmThreshold(alarmThreshold);
        sensorData.setAlarmFlag(alarmFlag);
        sensorData.setCollectTime(collectTime);
        sensorData.setStatus(alarmFlag);
        sensorData.setCreateBy("mqtt");
        sensorData.setCreateTime(now);
        sensorData.setUpdateBy("mqtt");
        sensorData.setUpdateTime(now);
        sensorData.setDeleted(0);
        sensorData.setRemark(mqttMessage.getRemark());
        return sensorData;
    }

    private boolean isAlarm(BigDecimal dataValue, BigDecimal alarmThreshold) {
        return dataValue != null && alarmThreshold != null && dataValue.compareTo(alarmThreshold) >= 0;
    }

    private void generateAlarm(MineSensorData sensorData) {
        if (sensorData.getId() == null) {
            return;
        }
        try {
            MineAlarmGenerateRequest request = new MineAlarmGenerateRequest();
            request.setSensorDataId(sensorData.getId());
            request.setSensorType(sensorData.getSensorType());
            request.setLimit(1);
            mineAlarmEventService.generate(request);
        } catch (Exception e) {
            log.error("MQTT sensor alarm generate failed, sensorDataId={}", sensorData.getId(), e);
        }
    }

    private String resolvePayload(Object payload) {
        if (payload instanceof byte[]) {
            return new String((byte[]) payload, StandardCharsets.UTF_8);
        }
        return String.valueOf(payload);
    }
}
