package com.internship.iotsimulatorkafka.service;

import com.internship.iotsimulatorkafka.config.KafkaListenerConfig;
import com.internship.iotsimulatorkafka.config.KafkaTopicConfig;
import com.internship.iotsimulatorkafka.dto.DeviceConnectionRequest;
import com.internship.iotsimulatorkafka.dto.DeviceConnectionResponse;
import com.internship.iotsimulatorkafka.dto.MetricsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class KafkaMessageService {

    @Autowired
    private KafkaTemplate <String, Object> kafkaTemplate;

    @Autowired
    private KafkaListenerConfig listenerConfig;

    public void startSession(DeviceConnectionRequest deviceConnectionRequest) {
        Message<DeviceConnectionRequest> message = MessageBuilder
                .withPayload(deviceConnectionRequest)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicConfig.START_SESSION)
                .build();
        kafkaTemplate.send(message);
        log.info("PRODUCER: Message sent to start session between machine#{} & device#{}", deviceConnectionRequest.getMachineId(), deviceConnectionRequest.getDeviceId());
    }

    public void endSession(Long sessionId) {
        Message<Long> message = MessageBuilder
                .withPayload(sessionId)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicConfig.END_SESSION)
                .build();
        kafkaTemplate.send(message);
        log.info("PRODUCER: Message sent to end IOT session#{}", sessionId);
    }

    public void sendMetrics(Long sessionId, MetricsRequest metricsRequest) {
        Message<MetricsRequest> message = MessageBuilder
                .withPayload(metricsRequest)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicConfig.METRICS_TOPIC)
                .setHeader(KafkaTopicConfig.SESSION_ID, sessionId)
                .build();
        kafkaTemplate.send(message);
        log.info("PRODUCER: Message sent with metrics {} for IOT session#{}", metricsRequest, sessionId);
    }


    @KafkaListener(topics= KafkaTopicConfig.RESPONSE, groupId = "myGroup", containerFactory = "factory")
    public DeviceConnectionResponse listenResponse(DeviceConnectionResponse deviceConnectionResponse) {
        log.info("CONSUMER: Got response for starting IOT session - IOT sessionId = {}", deviceConnectionResponse.getId());
        return deviceConnectionResponse;
    }


}
