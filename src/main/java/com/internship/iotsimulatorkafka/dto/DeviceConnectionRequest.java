package com.internship.iotsimulatorkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConnectionRequest implements Serializable {

    private Long machineId;
    private Long deviceId;
}
