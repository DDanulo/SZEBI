package com.example.server.Alerts.config;

import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.entities.AlertRule;
import com.example.server.Alerts.entities.Operator;
import com.example.server.Alerts.repositories.AlertRuleRepository;
import com.example.server.Alerts.services.AlertDataInput;
import com.example.server.Alerts.services.AlertPredictionService;
import com.example.server.Alerts.simulation.SensorData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class AlertDataSeeder implements CommandLineRunner {

    private final AlertRuleRepository alertRuleRepository;
    private final AlertDataInput alertDataInput;
    private final AlertPredictionService alertPredictionService;

    @Override
    public void run (String... args) throws Exception{


        if(alertRuleRepository.count() == 0){

            AlertRule rule1 = AlertRule.builder()
                    .ruleName("Wysoka Temperatura")
                    .metric("temperature")
                    .value(80.00)
                    .operator(Operator.GREATER_THAN)
                    .level(AlertLevel.WARNING)
                    .build();

            AlertRule rule2 = AlertRule.builder()
                    .ruleName("Krytyczna Temperatura")
                    .metric("temperature")
                    .value(110.00)
                    .operator(Operator.GREATER_THAN)
                    .level(AlertLevel.CRITICAL)
                    .build();

            AlertRule rule3 = AlertRule.builder()
                    .ruleName("Niskie Napięcie")
                    .metric("voltage")
                    .value(200.0)
                    .operator(Operator.LESS_THAN)
                    .level(AlertLevel.WARNING)
                    .build();

            AlertRule rule4 = AlertRule.builder()
                    .ruleName("Wysokie zużycie energii")
                    .metric("MWH")
                    .value(47.0)
                    .operator(Operator.GREATER_THAN)
                    .level(AlertLevel.WARNING)
                    .build();


            alertRuleRepository.saveAll(List.of(rule1, rule2, rule3, rule4));
        }


        alertDataInput.handleData(new SensorData("Piec_01", "temperature", 70.0, LocalDateTime.now()));
        alertDataInput.handleData(new SensorData("Piec_02", "temperature", 120.0, LocalDateTime.now()));
        alertPredictionService.checkForecast();

    }
}
