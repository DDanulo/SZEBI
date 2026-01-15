package com.example.server.Alerts.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String ruleName;
    @Enumerated(EnumType.STRING)
    private AlertLevel level;
    @Enumerated(EnumType.STRING)
    private Metric metric;
    private double value;
    @Enumerated(EnumType.STRING)
    private Operator operator;

    @Builder
    public AlertRule(UUID id, String ruleName, AlertLevel level, Metric metric, double value, Operator operator) {
        this.id = id;
        this.ruleName = ruleName;
        this.level = level;
        this.metric = metric;
        this.value = value;
        this.operator = operator;
    }
}
