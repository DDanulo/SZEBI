package com.example.server.Alerts.services;


import com.example.server.Alerts.api.IAlertObserver;
import com.example.server.Alerts.entities.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AlertPublisher {


    private final List<IAlertObserver> subscribers = new ArrayList<>();

    public void subscribe (IAlertObserver observer){
        subscribers.add(observer);
    }

    public void unsubscribe (IAlertObserver observer){
        subscribers.remove(observer);
    }

    public void notify(Alert alert) {
        for (IAlertObserver observer : subscribers) {

            CompletableFuture.runAsync(() -> {
                try {
                    observer.onAlert(alert);
                } catch (Exception e) {
                    log.error("Błąd podczas wysyłania alertu do subskrybenta: {}. Szczegóły: {}",
                            observer.getClass().getSimpleName(), e.getMessage());
                }
            });
        }
    }

}
