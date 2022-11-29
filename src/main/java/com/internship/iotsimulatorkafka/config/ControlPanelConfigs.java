package com.internship.iotsimulatorkafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlPanelConfigs {

    @Bean
    @ConfigurationProperties(prefix = "control-panel")
    public ControlPanelProperties controlPanelProperties(){
        return new ControlPanelProperties();
    }
}
