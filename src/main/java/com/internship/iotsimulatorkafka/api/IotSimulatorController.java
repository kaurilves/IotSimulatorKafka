package com.internship.iotsimulatorkafka.api;

import com.internship.iotsimulatorkafka.dto.DeviceConnectionRequest;
import com.internship.iotsimulatorkafka.dto.DeviceConnectionResponse;
import com.internship.iotsimulatorkafka.service.IotSimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/iotsimulator")
public class IotSimulatorController {

    @Autowired
    private IotSimulatorService iotSimulatorService;

    @PostMapping("/starts_session")
    public ResponseEntity<DeviceConnectionResponse> startSession(@RequestBody DeviceConnectionRequest deviceConnectionRequest) {
        iotSimulatorService.startSession(deviceConnectionRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/end_session/{sessionId}")
    public ResponseEntity<DeviceConnectionResponse> endSession(@PathVariable Long sessionId) {
        iotSimulatorService.endSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send_metrics/{sessionId}/{intervalInMilliSeconds}")
    public ResponseEntity<?> sendMetrics(
            @PathVariable Integer intervalInMilliSeconds,
            @PathVariable Long sessionId
    ) {
        iotSimulatorService.sendMetrics(sessionId, intervalInMilliSeconds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stop_sending_metrics/{sessionId}")
    public ResponseEntity<?> stopSendingMetrics(
            @PathVariable Long sessionId
    ) {
        iotSimulatorService.stopSendingMetrics(sessionId);
        return ResponseEntity.noContent().build();
    }
}
