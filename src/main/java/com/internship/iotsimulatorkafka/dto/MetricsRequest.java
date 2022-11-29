package com.internship.iotsimulatorkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRequest extends BaseRequest implements Serializable {

    private Integer value;
}