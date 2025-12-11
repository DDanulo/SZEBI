package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {


    private final AlertPublisher alertPublisher;
    private final AlertRepository alertRepository;


    public void createSosAlert(String message){
        Alert alert = Alert.builder().level(AlertLevel.SOS).message(message).source("User SOS").timestamp(LocalDateTime.now()).build();
        alertPublisher.notify(alert);
    }

    public List<Alert> getAllAlerts(){
        return alertRepository.findAll();
    }


}
