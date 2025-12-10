package com.example.server.Alerts.services;


import com.example.server.Alerts.api.IAlertObserver;
import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.repositories.AlertRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertLogger implements IAlertObserver {

    private final AlertRepository alertRepository;
    private final AlertPublisher alertPublisher;

    @PostConstruct
    public void init(){
        alertPublisher.subscribe(this);
    }

    @Override
    public void onAlert(Alert alert){
        alertRepository.save(alert);
    }

}
