package com.internship.iotsimulatorkafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String SESSION_ID = "session_id";

    public static final String METRICS_TOPIC = "simulator_send_metrics";
    public static final String START_SESSION = "simulator_start_session";
    public static final String END_SESSION = "simulator_end_session";
    public static final String RESPONSE = "controller_send_response";

    @Bean
    public NewTopic sendMetric(){
        return TopicBuilder
                .name(METRICS_TOPIC)
                .build();
    }

    @Bean
    public NewTopic startSession(){
        return TopicBuilder
                .name(START_SESSION)
                .build();
    }

    @Bean
    public NewTopic endSession(){
        return TopicBuilder
                .name(END_SESSION)
                .build();
    }

    @Bean
    public NewTopic sendResponse(){
        return TopicBuilder
                .name(RESPONSE)
                .build();
    }


}
