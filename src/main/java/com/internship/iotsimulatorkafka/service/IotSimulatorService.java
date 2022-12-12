package com.internship.iotsimulatorkafka.service;

import com.internship.iotsimulatorkafka.config.AsyncConfiguration;
import com.internship.iotsimulatorkafka.config.MetricsSimulator;
import com.internship.iotsimulatorkafka.dto.DeviceConnectionRequest;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;


@Service
@Slf4j
public class IotSimulatorService {

    private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Autowired
    private AsyncConfiguration asyncConfiguration;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    public IotSimulatorService() {
    }


    public void startSession(DeviceConnectionRequest deviceConnectionRequest) {
        kafkaMessageService.startSession(deviceConnectionRequest);
    }

    public void endSession(Long sessionId) {
        kafkaMessageService.endSession(sessionId);
        if (tasks.get(sessionId) != null) {
            tasks.get(sessionId).cancel(false);
        }
    }

    @Async("asyncScheduler")
    public void sendMetrics(Long sessionId, Integer intervalInMilliSeconds) {
        var metricsSimulator = new MetricsSimulator(sessionId, kafkaMessageService);
        tasks.put(sessionId, asyncConfiguration.taskScheduler().scheduleAtFixedRate(metricsSimulator, intervalInMilliSeconds));
    }

    public void stopSendingMetrics(Long sessionId) {
        tasks.get(sessionId).cancel(false);
        log.info("MESSAGE: Sending metrics to IOT session#{} stopped!", sessionId);
    }

    @PreDestroy
    private void stopAllSessionsFromSendingMetrics() {
        for (Map.Entry<Long, ScheduledFuture<?>> task :
                tasks.entrySet()) {
            stopSendingMetrics(task.getKey());
        }
    }
}
