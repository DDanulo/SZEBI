package com.example.server.Alerts.simulation;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
public record SensorData(
        String source,
        String metric,
        double value,
        LocalDateTime timestamp
) {}