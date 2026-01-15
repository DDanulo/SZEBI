package com.example.server.Alerts.controllers;


import com.example.server.Alerts.dto.SosReport;
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


    @GetMapping("/logs")
    public List<Alert> getAlertsLogs(){
        return alertService.getAllAlerts();
    }

    @PostMapping("/sos")
    public ResponseEntity<String> reportSos(@RequestBody SosReport sosReport){

        if(sosReport.getUserID() == null)
        {
            return ResponseEntity.badRequest().body("BŁAD: Brak ID użytkownika");
        }

        alertService.createSosAlert(sosReport);

        return ResponseEntity.ok("Zgłoszenie zostało poprawnie przyjete");
    }

}
