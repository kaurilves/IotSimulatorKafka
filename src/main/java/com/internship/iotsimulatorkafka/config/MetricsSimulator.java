package com.internship.iotsimulatorkafka.config;

import com.internship.iotsimulatorkafka.dto.MetricsRequest;
import com.internship.iotsimulatorkafka.service.KafkaMessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Random;


@Slf4j
@Data
@RequiredArgsConstructor
public class MetricsSimulator implements Runnable {

    private final Long sessionId;
    private final KafkaMessageService kafkaMessageService;


    @Override
    public void run() {
        Random random = new Random();
        Integer value = random.nextInt();
        MetricsRequest metricsRequest = new MetricsRequest();
        metricsRequest.setValue(value);
        kafkaMessageService.sendMetrics(sessionId, metricsRequest);
    }
}
