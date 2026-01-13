package com.example.server.Alerts.services;


import com.example.server.Alerts.dto.SosReport;
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


    public void createSosAlert(SosReport sosReport){

        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("SOS Alert");

        if (sosReport.getMessage() != null && !sosReport.getMessage().isEmpty()){
            msgBuilder.append("- TREŚĆ: ").append(sosReport.getMessage());
        }

        Alert alert = Alert.builder()
                .level(AlertLevel.SOS)
                .userId(sosReport.getUserID())
                .location(sosReport.getLocation())
                .message(msgBuilder.toString())
                .source("User SOS")
                .timestamp(LocalDateTime.now())
                .build();


        alertPublisher.notify(alert);
    }

    public List<Alert> getAllAlerts(){
        return alertRepository.findAll();
    }


}
