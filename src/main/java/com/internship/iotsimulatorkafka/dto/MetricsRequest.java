package com.internship.iotsimulatorkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRequest implements Serializable {

    private Integer value;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
