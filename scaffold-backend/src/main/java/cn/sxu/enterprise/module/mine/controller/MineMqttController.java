package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.config.MineMqttProperties;
import cn.sxu.enterprise.module.mine.dto.MineMqttBatchSimulateRequest;
import cn.sxu.enterprise.module.mine.dto.MineSensorMqttMessage;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/mine/mqtt")
@ConditionalOnProperty(prefix = "enterprise.mine.mqtt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MineMqttController {

    private final MessageChannel mineMqttOutboundChannel;

    private final ObjectMapper objectMapper;

    private final MineMqttProperties mineMqttProperties;

    private final MineSensorMapper mineSensorMapper;

    public MineMqttController(@Qualifier("mineMqttOutboundChannel") MessageChannel mineMqttOutboundChannel,
                              ObjectMapper objectMapper,
                              MineMqttProperties mineMqttProperties,
                              MineSensorMapper mineSensorMapper) {
        this.mineMqttOutboundChannel = mineMqttOutboundChannel;
        this.objectMapper = objectMapper;
        this.mineMqttProperties = mineMqttProperties;
        this.mineSensorMapper = mineSensorMapper;
    }

    @OperLog(title = "智能矿山-MQTT", businessType = "模拟发布")
    @PostMapping("/simulate-publish")
    public ApiResult<MineSensorMqttMessage> simulatePublish(@Valid @RequestBody MineSensorMqttMessage request) {
        if (request.getCollectTime() == null) {
            request.setCollectTime(LocalDateTime.now());
        }

        try {
            boolean sent = publishMqttMessage(request);
            if (!sent) {
                return ApiResult.error(500, "MQTT消息发送失败");
            }
            return ApiResult.success(request);
        } catch (Exception e) {
            return ApiResult.error(500, "MQTT模拟发布失败：" + e.getMessage());
        }
    }

    @OperLog(title = "智能矿山-MQTT", businessType = "批量模拟发布")
    @PostMapping("/simulate-batch")
    public ApiResult<List<MineSensorMqttMessage>> simulateBatch(@Valid @RequestBody MineMqttBatchSimulateRequest request) {
        int count = request.getCount() == null ? 10 : request.getCount();
        int intervalMillis = request.getIntervalMillis() == null ? 0 : request.getIntervalMillis();

        LambdaQueryWrapper<MineSensor> queryWrapper = new LambdaQueryWrapper<MineSensor>()
                .eq(MineSensor::getStatus, 0)
                .orderByAsc(MineSensor::getId);

        if (StringUtils.hasText(request.getSensorType())) {
            queryWrapper.eq(MineSensor::getSensorType, request.getSensorType().trim());
        }

        List<MineSensor> sensors = mineSensorMapper.selectList(queryWrapper);
        if (sensors.isEmpty()) {
            return ApiResult.error(500, "没有可用于MQTT批量模拟的正常传感器");
        }

        List<MineSensorMqttMessage> publishedMessages = new ArrayList<>();

        try {
            for (int i = 0; i < count; i++) {
                MineSensor sensor = sensors.get(ThreadLocalRandom.current().nextInt(sensors.size()));

                MineSensorMqttMessage mqttMessage = new MineSensorMqttMessage();
                mqttMessage.setSensorCode(sensor.getSensorCode());
                mqttMessage.setDataValue(buildRandomValue(sensor));
                mqttMessage.setCollectTime(LocalDateTime.now());
                mqttMessage.setRemark(buildBatchRemark(request.getRemark(), i + 1, count));

                boolean sent = publishMqttMessage(mqttMessage);
                if (!sent) {
                    return ApiResult.error(500, "第" + (i + 1) + "条MQTT消息发送失败");
                }

                publishedMessages.add(mqttMessage);

                if (intervalMillis > 0 && i < count - 1) {
                    Thread.sleep(intervalMillis);
                }
            }

            return ApiResult.success(publishedMessages);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ApiResult.error(500, "MQTT批量模拟发布被中断");
        } catch (Exception e) {
            return ApiResult.error(500, "MQTT批量模拟发布失败：" + e.getMessage());
        }
    }

    private boolean publishMqttMessage(MineSensorMqttMessage mqttMessage) throws Exception {
        String payload = objectMapper.writeValueAsString(mqttMessage);
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, mineMqttProperties.getTopic())
                .setHeader(MqttHeaders.QOS, mineMqttProperties.getQos())
                .build();

        return mineMqttOutboundChannel.send(message, mineMqttProperties.getCompletionTimeout());
    }

    private BigDecimal buildRandomValue(MineSensor sensor) {
        BigDecimal minValue = sensor.getMinValue() == null ? BigDecimal.ZERO : sensor.getMinValue();
        BigDecimal maxValue = sensor.getMaxValue() == null ? new BigDecimal("100") : sensor.getMaxValue();

        if (maxValue.compareTo(minValue) <= 0) {
            maxValue = minValue.add(new BigDecimal("10"));
        }

        BigDecimal alarmThreshold = sensor.getAlarmThreshold();
        boolean makeAlarm = alarmThreshold != null && ThreadLocalRandom.current().nextInt(100) < 30;

        if (makeAlarm && alarmThreshold.compareTo(maxValue) <= 0) {
            BigDecimal alarmMinValue = alarmThreshold.max(minValue);
            if (alarmMinValue.compareTo(maxValue) >= 0) {
                return maxValue.setScale(2, RoundingMode.HALF_UP);
            }
            return randomDecimal(alarmMinValue, maxValue);
        }

        return randomDecimal(minValue, maxValue);
    }

    private BigDecimal randomDecimal(BigDecimal minValue, BigDecimal maxValue) {
        double value = ThreadLocalRandom.current().nextDouble(minValue.doubleValue(), maxValue.doubleValue());
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private String buildBatchRemark(String remark, int current, int total) {
        String prefix = StringUtils.hasText(remark) ? remark : "M1-08 MQTT批量模拟上报";
        return prefix + "，第" + current + "/" + total + "条";
    }
}