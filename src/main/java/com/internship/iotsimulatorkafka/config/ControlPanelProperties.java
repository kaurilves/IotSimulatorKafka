package com.internship.iotsimulatorkafka.config;

import lombok.Data;

@Data
public class ControlPanelProperties {
    private String controllerPath;
    private String metricsPath;
}
