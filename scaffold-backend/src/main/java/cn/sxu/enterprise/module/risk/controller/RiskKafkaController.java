package cn.sxu.enterprise.module.risk.controller;

import cn.sxu.enterprise.common.core.result.ApiResult;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.risk.dto.RiskKafkaBatchSimulateRequest;
import cn.sxu.enterprise.module.risk.dto.RiskTransactionKafkaMessage;
import cn.sxu.enterprise.module.risk.service.RiskTransactionKafkaProducer;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk/kafka")
@ConditionalOnProperty(prefix = "enterprise.risk.kafka", name = "enabled", havingValue = "true")
public class RiskKafkaController {

    private final RiskTransactionKafkaProducer riskTransactionKafkaProducer;

    public RiskKafkaController(RiskTransactionKafkaProducer riskTransactionKafkaProducer) {
        this.riskTransactionKafkaProducer = riskTransactionKafkaProducer;
    }

    @OperLog(title = "银行风控-Kafka", businessType = "模拟发布")
    @PostMapping("/simulate-publish")
    public ApiResult<RiskTransactionKafkaMessage> simulatePublish(
            @RequestBody(required = false) RiskTransactionKafkaMessage request
    ) {
        return ApiResult.success(riskTransactionKafkaProducer.publish(request));
    }

    @OperLog(title = "银行风控-Kafka", businessType = "批量模拟发布")
    @PostMapping("/simulate-batch")
    public ApiResult<Integer> simulateBatch(
            @Valid @RequestBody(required = false) RiskKafkaBatchSimulateRequest request
    ) {
        return ApiResult.success(riskTransactionKafkaProducer.publishBatch(request));
    }
}
