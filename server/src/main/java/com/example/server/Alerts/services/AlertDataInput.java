package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.simulation.IData;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertDataInput implements IData {

    private final AlertPublisher alertPublisher;
    private final AlertRuleEngine alertRuleEngine;

    @Override
    public void handleData(SensorData data) {
        AlertLevel detectedLevel = alertRuleEngine.checkAlertLevel(data);
        Alert previosusAlert = null;

        if(detectedLevel != null){

            Alert alert = Alert.builder()
                    .userId(data.userID())
                    .location(data.location())
                    .level(detectedLevel)
                    .source(data.source())
                    .message("Wykryto anomalie dla " + data.metric() + " Wartość: "+ data.value())
                    .timestamp(data.timestamp())
                    .build();

            if (!previosusAlert.equals(alert)) {

                previosusAlert = alert;
                alertPublisher.notify(alert);

            }


        } else {

        }

    }
}
