package com.example.server.Alerts.api;

import com.example.server.Alerts.entities.Alert;


public interface IAlertObserver {

    void onAlert(Alert alert);
}
