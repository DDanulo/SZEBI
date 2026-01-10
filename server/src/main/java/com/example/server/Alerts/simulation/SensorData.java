package com.example.server.Alerts.simulation;

import com.example.server.Alerts.entities.Metric;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
public record SensorData(
        String source,
        Metric metric,
        double value,
        LocalDateTime timestamp
) {}