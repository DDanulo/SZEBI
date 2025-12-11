package com.example.server.Alerts.services;

import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.entities.AlertRule;
import com.example.server.Alerts.repositories.AlertRuleRepository;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertRuleEngine {

    private final AlertRuleRepository alertRuleRepository;

    public AlertLevel checkAlertLevel(SensorData data) {

        List<AlertRule> rules = alertRuleRepository.findByMetric(data.metric());


        AlertLevel highestDetectedLevel = null;


        for (AlertRule rule : rules) {
            boolean isBroken = rule.getOperator().check(data.value(), rule.getValue());

            if (isBroken) {
                if (highestDetectedLevel == null || rule.getLevel().ordinal() > highestDetectedLevel.ordinal()) {
                    highestDetectedLevel = rule.getLevel();
                }
            }
        }

       // System.out.println("Sprawdzono czy istnie alert dla " + data.source() + " i wynosi on: " + highestDetectedLevel);
        return highestDetectedLevel;
    }



}