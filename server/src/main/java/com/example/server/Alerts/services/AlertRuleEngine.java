package com.example.server.Alerts.services;

import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.entities.AlertRule;
import com.example.server.Alerts.entities.Metric;
import com.example.server.Alerts.repositories.AlertRuleRepository;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertRuleEngine {

    private final AlertRuleRepository alertRuleRepository;

    public AlertLevel checkAlertLevel(SensorData data) {
        try {
            Metric metricEnum = Metric.valueOf(data.metric().name().toUpperCase());

            List<AlertRule> rules = alertRuleRepository.findByMetric(metricEnum);
            AlertLevel highestDetectedLevel = null;

            for (AlertRule rule : rules) {
                boolean isBroken = rule.getOperator().check(data.value(), rule.getValue());

                if (isBroken) {
                    if (highestDetectedLevel == null || rule.getLevel().ordinal() > highestDetectedLevel.ordinal()) {
                        highestDetectedLevel = rule.getLevel();
                    }
                }
            }
            return highestDetectedLevel;

        } catch (IllegalArgumentException e) {
            log.warn("Otrzymano nieznaną metrykę: {}", data.metric());
            return null;
        }
    }

    public List<AlertRule> getAllRules(){
        return alertRuleRepository.findAll();
    }

    public AlertRule addRule(AlertRule rule){
        return alertRuleRepository.save(rule);
    }

    public void deleteRule(UUID id){
        alertRuleRepository.deleteById(id);
    }
}