package cn.sxu.enterprise.module.risk.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import cn.sxu.enterprise.module.risk.config.RiskKafkaProperties;
import cn.sxu.enterprise.module.risk.dto.RiskKafkaBatchSimulateRequest;
import cn.sxu.enterprise.module.risk.dto.RiskTransactionKafkaMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@ConditionalOnProperty(prefix = "enterprise.risk.kafka", name = "enabled", havingValue = "true")
public class RiskTransactionKafkaProducer {

    private static final DateTimeFormatter CODE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final KafkaTemplate<String, String> riskKafkaTemplate;

    private final RiskKafkaProperties riskKafkaProperties;

    private final ObjectMapper objectMapper;

    public RiskTransactionKafkaProducer(KafkaTemplate<String, String> riskKafkaTemplate,
                                        RiskKafkaProperties riskKafkaProperties,
                                        ObjectMapper objectMapper) {
        this.riskKafkaTemplate = riskKafkaTemplate;
        this.riskKafkaProperties = riskKafkaProperties;
        this.objectMapper = objectMapper;
    }

    public RiskTransactionKafkaMessage publish(RiskTransactionKafkaMessage message) {
        RiskTransactionKafkaMessage finalMessage = completeMessage(message);
        String payload = toJson(finalMessage);
        riskKafkaTemplate.send(
                riskKafkaProperties.getTransactionTopic(),
                finalMessage.getTransactionNo(),
                payload
        );
        return finalMessage;
    }

    public Integer publishBatch(RiskKafkaBatchSimulateRequest request) {
        RiskKafkaBatchSimulateRequest safeRequest = request == null ? new RiskKafkaBatchSimulateRequest() : request;
        int count = safeRequest.getCount() == null ? 10 : safeRequest.getCount();
        count = Math.max(1, Math.min(count, 100));

        for (int i = 0; i < count; i++) {
            RiskTransactionKafkaMessage message = buildRandomMessage(safeRequest);
            publish(message);
        }
        return count;
    }

    private RiskTransactionKafkaMessage completeMessage(RiskTransactionKafkaMessage source) {
        RiskTransactionKafkaMessage message = source == null ? new RiskTransactionKafkaMessage() : source;

        if (!StringUtils.hasText(message.getTransactionNo())) {
            message.setTransactionNo(generateTransactionNo());
        }
        if (!StringUtils.hasText(message.getAccountNo())) {
            message.setAccountNo("ACC-" + ThreadLocalRandom.current().nextInt(100001, 999999));
        }
        if (message.getCustomerId() == null) {
            message.setCustomerId(ThreadLocalRandom.current().nextLong(10001L, 99999L));
        }
        if (!StringUtils.hasText(message.getCustomerName())) {
            message.setCustomerName("客户" + message.getCustomerId());
        }
        if (!StringUtils.hasText(message.getMerchantId())) {
            message.setMerchantId("MCH-" + ThreadLocalRandom.current().nextInt(1001, 9999));
        }
        if (!StringUtils.hasText(message.getMerchantName())) {
            message.setMerchantName("Kafka模拟商户");
        }
        if (!StringUtils.hasText(message.getTransactionType())) {
            message.setTransactionType(randomOne("PAYMENT", "TRANSFER", "WITHDRAW", "CONSUME"));
        }
        if (!StringUtils.hasText(message.getChannel())) {
            message.setChannel(randomOne("APP", "WEB", "ATM", "POS"));
        }
        if (message.getAmount() == null) {
            message.setAmount(randomAmount(BigDecimal.valueOf(100), BigDecimal.valueOf(20000)));
        }
        if (!StringUtils.hasText(message.getCurrency())) {
            message.setCurrency("CNY");
        }
        if (!StringUtils.hasText(message.getIpAddr())) {
            message.setIpAddr("192.168.10." + ThreadLocalRandom.current().nextInt(2, 250));
        }
        if (!StringUtils.hasText(message.getDeviceId())) {
            message.setDeviceId("DEVICE-" + ThreadLocalRandom.current().nextInt(1001, 9999));
        }
        if (!StringUtils.hasText(message.getLocation())) {
            message.setLocation(randomOne("山西太原", "山西大同", "北京", "上海", "境外"));
        }
        if (message.getTransactionTime() == null) {
            message.setTransactionTime(LocalDateTime.now());
        }
        if (message.getTransactionStatus() == null) {
            message.setTransactionStatus(0);
        }
        if (!StringUtils.hasText(message.getRemark())) {
            message.setRemark("R3-05 Kafka模拟交易消息");
        }

        return message;
    }

    private RiskTransactionKafkaMessage buildRandomMessage(RiskKafkaBatchSimulateRequest request) {
        RiskTransactionKafkaMessage message = new RiskTransactionKafkaMessage();
        message.setTransactionType(StringUtils.hasText(request.getTransactionType())
                ? request.getTransactionType()
                : randomOne("PAYMENT", "TRANSFER", "WITHDRAW", "CONSUME"));
        message.setChannel(StringUtils.hasText(request.getChannel())
                ? request.getChannel()
                : randomOne("APP", "WEB", "ATM", "POS"));
        message.setLocation(StringUtils.hasText(request.getLocation())
                ? request.getLocation()
                : randomOne("山西太原", "山西大同", "北京", "上海", "境外"));
        message.setAmount(randomAmount(
                request.getMinAmount() == null ? BigDecimal.valueOf(100) : request.getMinAmount(),
                request.getMaxAmount() == null ? BigDecimal.valueOf(20000) : request.getMaxAmount()
        ));
        message.setRemark(StringUtils.hasText(request.getRemark())
                ? request.getRemark()
                : "R3-05 Kafka批量模拟交易消息");
        return message;
    }

    private String generateTransactionNo() {
        return "KAFKA-TXN-" + LocalDateTime.now().format(CODE_TIME_FORMATTER)
                + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private String randomOne(String... values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    private BigDecimal randomAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        BigDecimal min = minAmount == null ? BigDecimal.valueOf(100) : minAmount;
        BigDecimal max = maxAmount == null ? BigDecimal.valueOf(20000) : maxAmount;

        if (max.compareTo(min) < 0) {
            BigDecimal tmp = min;
            min = max;
            max = tmp;
        }

        double value = ThreadLocalRandom.current().nextDouble(min.doubleValue(), max.doubleValue() + 1);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private String toJson(RiskTransactionKafkaMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Kafka交易消息序列化失败", e);
        }
    }
}
