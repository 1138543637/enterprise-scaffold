package cn.sxu.enterprise.module.risk.listener;

import java.time.LocalDateTime;

import cn.sxu.enterprise.module.risk.dto.RiskTransactionKafkaMessage;
import cn.sxu.enterprise.module.risk.entity.RiskTransaction;
import cn.sxu.enterprise.module.risk.mapper.RiskTransactionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConditionalOnProperty(prefix = "enterprise.risk.kafka", name = "enabled", havingValue = "true")
public class RiskTransactionKafkaListener {

    private final RiskTransactionMapper riskTransactionMapper;

    private final ObjectMapper objectMapper;

    public RiskTransactionKafkaListener(RiskTransactionMapper riskTransactionMapper,
                                        ObjectMapper objectMapper) {
        this.riskTransactionMapper = riskTransactionMapper;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${enterprise.risk.kafka.transaction-topic}",
            groupId = "${enterprise.risk.kafka.consumer-group-id}",
            containerFactory = "riskKafkaListenerContainerFactory"
    )
    public void onMessage(String payload) {
        try {
            RiskTransactionKafkaMessage message =
                    objectMapper.readValue(payload, RiskTransactionKafkaMessage.class);

            if (!StringUtils.hasText(message.getTransactionNo())) {
                return;
            }

            Long exists = riskTransactionMapper.selectCount(
                    new LambdaQueryWrapper<RiskTransaction>()
                            .eq(RiskTransaction::getTransactionNo, message.getTransactionNo())
                            .eq(RiskTransaction::getDeleted, 0)
            );

            if (exists != null && exists > 0) {
                return;
            }

            RiskTransaction entity = new RiskTransaction();
            entity.setTransactionNo(message.getTransactionNo());
            entity.setAccountNo(message.getAccountNo());
            entity.setCustomerId(message.getCustomerId());
            entity.setCustomerName(message.getCustomerName());
            entity.setMerchantId(message.getMerchantId());
            entity.setMerchantName(message.getMerchantName());
            entity.setTransactionType(message.getTransactionType());
            entity.setChannel(message.getChannel());
            entity.setAmount(message.getAmount());
            entity.setCurrency(message.getCurrency());
            entity.setIpAddr(message.getIpAddr());
            entity.setDeviceId(message.getDeviceId());
            entity.setLocation(message.getLocation());
            entity.setTransactionTime(message.getTransactionTime() == null ? LocalDateTime.now() : message.getTransactionTime());
            entity.setTransactionStatus(message.getTransactionStatus() == null ? 0 : message.getTransactionStatus());
            entity.setRiskFlag(0);
            entity.setStatus(0);
            entity.setCreateBy("kafka");
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateBy("kafka");
            entity.setUpdateTime(LocalDateTime.now());
            entity.setDeleted(0);
            entity.setRemark(message.getRemark());

            riskTransactionMapper.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
