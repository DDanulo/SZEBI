package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.simulation.IData;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertDataInput implements IData {

    private final AlertPublisher alertPublisher;
    private final AlertRuleEngine alertRuleEngine;

    @Override
    public void handleData(SensorData data) {
        AlertLevel detectedLevel = alertRuleEngine.checkAlertLevel(data);

        if(detectedLevel != null){

            Alert alert = Alert.builder()
                    .level(detectedLevel)
                    .source(data.source())
                    .message("Wykryto anomalie dla " + data.metric() + " Wartość: "+ data.value())
                    .timestamp(LocalDateTime.now())
                    .build();

            alertPublisher.notify(alert);

        } else {

        }

    }
}
