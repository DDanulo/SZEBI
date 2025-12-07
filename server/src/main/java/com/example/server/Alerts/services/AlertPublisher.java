package com.example.server.Alerts.services;


import com.example.server.Alerts.api.IAlertObserver;
import com.example.server.Alerts.entities.Alert;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlertPublisher {


    private final List<IAlertObserver> subscribers = new ArrayList<>();

    public void subscribe (IAlertObserver observer){
        subscribers.add(observer);
    }

    public void unsubscribe (IAlertObserver observer){
        subscribers.remove(observer);
    }

    public void notify(Alert alert){
        for(IAlertObserver observer : subscribers){
            observer.onAlert(alert);
        }
    }

}
