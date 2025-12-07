package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertService {


    private final AlertPublisher alertPublisher;

    public void createSosAlert(String message){
        Alert alert = Alert.builder().level(AlertLevel.SOS).message(message).source("User SOS").timestamp(LocalDateTime.now()).build();
        alertPublisher.notify(alert);
    }



}
