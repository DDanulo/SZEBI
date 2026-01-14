package com.example.server.Alerts.services;

import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.simulation.IData;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertDataInput implements IData {

    private final AlertPublisher alertPublisher;
    private final AlertRuleEngine alertRuleEngine;
    private Alert previousAlert = null;

    @Override
    public void handleData(SensorData data) {
        AlertLevel detectedLevel = alertRuleEngine.checkAlertLevel(data);

        if (detectedLevel != null) {

            Alert currentAlert = Alert.builder()
                    .userId(data.userID())
                    .location(data.location())
                    .level(detectedLevel)
                    .source(data.source())
                    .message("Wykryto anomalie dla " + data.metric() + " Wartość: " + data.value())
                    .timestamp(data.timestamp())
                    .build();

            if (previousAlert == null || !areAlertsSimilar(previousAlert, currentAlert)) {

                previousAlert = currentAlert;
                alertPublisher.notify(currentAlert);
            } else {
            }

        }
    }

    private boolean areAlertsSimilar(Alert oldAlert, Alert newAlert) {
        if (oldAlert == null) return false;

        return oldAlert.getLevel() == newAlert.getLevel() &&
                oldAlert.getSource().equals(newAlert.getSource()) &&
                oldAlert.getMessage().equals(newAlert.getMessage());
    }
}