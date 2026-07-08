package cn.sxu.enterprise.module.risk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enterprise.risk.kafka")
public class RiskKafkaProperties {

    private Boolean enabled = false;

    private String bootstrapServers = "localhost:9092";

    private String transactionTopic = "risk.transaction.events";

    private String consumerGroupId = "enterprise-scaffold-risk-consumer";

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTransactionTopic() {
        return transactionTopic;
    }

    public void setTransactionTopic(String transactionTopic) {
        this.transactionTopic = transactionTopic;
    }

    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    public void setConsumerGroupId(String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;
    }
}
