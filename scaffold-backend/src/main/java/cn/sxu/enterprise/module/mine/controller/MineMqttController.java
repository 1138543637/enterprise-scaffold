package cn.sxu.enterprise.module.mine.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.mine.config.MineMqttProperties;
import cn.sxu.enterprise.module.mine.dto.MineSensorMqttMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/mine/mqtt")
@ConditionalOnProperty(prefix = "enterprise.mine.mqtt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MineMqttController {

    private final MessageChannel mineMqttOutboundChannel;

    private final ObjectMapper objectMapper;

    private final MineMqttProperties mineMqttProperties;

    public MineMqttController(@Qualifier("mineMqttOutboundChannel") MessageChannel mineMqttOutboundChannel,
                              ObjectMapper objectMapper,
                              MineMqttProperties mineMqttProperties) {
        this.mineMqttOutboundChannel = mineMqttOutboundChannel;
        this.objectMapper = objectMapper;
        this.mineMqttProperties = mineMqttProperties;
    }

    @OperLog(title = "智能矿山-MQTT", businessType = "模拟发布")
    @PostMapping("/simulate-publish")
    public ApiResult<MineSensorMqttMessage> simulatePublish(@Valid @RequestBody MineSensorMqttMessage request) {
        if (request.getCollectTime() == null) {
            request.setCollectTime(LocalDateTime.now());
        }

        try {
            String payload = objectMapper.writeValueAsString(request);
            Message<String> message = MessageBuilder.withPayload(payload)
                    .setHeader(MqttHeaders.TOPIC, mineMqttProperties.getTopic())
                    .setHeader(MqttHeaders.QOS, mineMqttProperties.getQos())
                    .build();

            boolean sent = mineMqttOutboundChannel.send(message, 5000);
            if (!sent) {
                return ApiResult.error(500, "MQTT消息发送失败");
            }
            return ApiResult.success(request);
        } catch (Exception e) {
            return ApiResult.error(500, "MQTT模拟发布失败：" + e.getMessage());
        }
    }
}
