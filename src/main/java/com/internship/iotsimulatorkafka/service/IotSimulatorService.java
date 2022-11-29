package com.internship.iotsimulatorkafka.service;

import com.internship.iotsimulatorkafka.config.AsyncConfiguration;
import com.internship.iotsimulatorkafka.config.ControlPanelProperties;
import com.internship.iotsimulatorkafka.dto.BaseRequest;
import com.internship.iotsimulatorkafka.dto.BaseResponse;
import com.internship.iotsimulatorkafka.dto.MetricsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@Slf4j
public class IotSimulatorService {


    @Autowired
    private ControlPanelProperties controlPanelProperties;
    private static final Map<Long, AsyncConfiguration> tasks = new HashMap<>();

    @Autowired
    private AsyncConfiguration asyncConfiguration;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    public BaseResponse startSession(BaseRequest baseRequest) {
        String url = String.format("%s/%s/%s", controlPanelProperties.getControllerPath(), baseRequest.getMachineId(), baseRequest.getDeviceId());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sendPost(url, baseRequest).getSessionId());
        return baseResponse;
    }

    public BaseResponse endSession(Long sessionId) {
        BaseRequest baseRequest = new BaseRequest();
        String url = String.format("%s/%s", controlPanelProperties.getControllerPath(), sessionId);
        sendPost(url, baseRequest);
        stopSendingMetrics(sessionId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sessionId);
        return baseResponse;
    }

    // Sending metrics using Timer

   /* public BaseResponse sendMetrics(Long sessionId) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                Integer value = random.nextInt();
                MetricsRequest metricsRequest = new MetricsRequest(value);
                String url = String.format("%s/%s/%s", controlPanelProperties.getControllerPath(), sessionId, controlPanelProperties.getMetricsPath());
                sendPost(url, metricsRequest);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 10000, 10000);
        tasks.put(sessionId, timer);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sessionId);
        return baseResponse;
    } */

    // Sending metrics using ExecutorService and ScheduledThreadPoolExecutor
  /*
 public BaseResponse sendMetrics(Long sessionId, Integer intervalInSeconds) {
        Runnable task = () -> {
                Random random = new Random();
                Integer value = random.nextInt();
                MetricsRequest metricsRequest = new MetricsRequest(value);
                String url = String.format("%s/%s/%s", controlPanelProperties.getControllerPath(), sessionId, controlPanelProperties.getMetricsPath());
                sendPost(url, metricsRequest);
            };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        executor.scheduleAtFixedRate(task, 0, intervalInSeconds, TimeUnit.SECONDS);
        tasks.put(sessionId, executor);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sessionId);
        return baseResponse;
    } */
    @Async("asyncScheduler")
    public BaseResponse sendMetrics(Long sessionId, Integer intervalInSeconds) {
        Runnable task = () -> {
            Random random = new Random();
            Integer value = random.nextInt();
            MetricsRequest metricsRequest = new MetricsRequest(value);
            String url = String.format("%s/%s/%s", controlPanelProperties.getControllerPath(), sessionId, controlPanelProperties.getMetricsPath());
            sendPost(url, metricsRequest);
            log.info("sending metrics in session# {}", sessionId);
        };
        asyncConfiguration.taskScheduler().scheduleAtFixedRate(task, intervalInSeconds);
        tasks.put(sessionId, asyncConfiguration); // VALE!!!
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sessionId);
        return baseResponse;
    }


    public BaseResponse stopSendingMetrics(Long sessionId) {
        log.info("Metric send for session#{} stopped! ", sessionId);
        // tasks.get(sessionId).cancel();
        tasks.get(sessionId).taskScheduler().shutdown(); // VALE!!!
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSessionId(sessionId);
        return baseResponse;
    }


    private BaseResponse sendPost(String url, BaseRequest body) {
        return getRestTemplate().postForObject(url, body, BaseResponse.class);
    }

    @PreDestroy
    private void stopAllSessionsFromSendingMetrics() {
        for (Map.Entry<Long, AsyncConfiguration> task : tasks.entrySet()) {
            log.info("Metric send for session#{} stopped! ", task.getKey());  // VALE!!!
            tasks.get(task.getKey()).taskScheduler().shutdown();
        }
    }
}
