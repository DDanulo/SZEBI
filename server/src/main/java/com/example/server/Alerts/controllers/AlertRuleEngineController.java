package com.example.server.Alerts.controllers;

import com.example.server.Alerts.entities.AlertRule;
import com.example.server.Alerts.services.AlertRuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AlertRuleEngineController {

    private final AlertRuleEngine alertRuleEngine;

@GetMapping
    private List<AlertRule> getAllRules(){
    return alertRuleEngine.getAllRules();
}

@PostMapping
    private final AlertRule saveRule(@RequestBody AlertRule rule){
    return alertRuleEngine.addRule(rule);
}

@DeleteMapping("/{id}")
    private final void removeRule(@PathVariable UUID id){
    alertRuleEngine.deleteRule(id);
}

}
