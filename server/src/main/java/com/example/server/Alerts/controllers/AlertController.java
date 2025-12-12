package com.example.server.Alerts.controllers;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AlertController {

    private final AlertService alertService;

    public ResponseEntity<String> triggerSos(@RequestBody Map<String, String> payload) {

        String message = payload.get("message");
        if (message == null || message.isEmpty()) {
            message = "NIEOKREŚLONE ZGŁOSZENIE";
        }
        alertService.createSosAlert(message);

        return ResponseEntity.ok("SOS przyjęte");
    }

    @GetMapping("/logs")
    public List<Alert> getAlertsLogs(){
        return alertService.getAllAlerts();
    }


}
