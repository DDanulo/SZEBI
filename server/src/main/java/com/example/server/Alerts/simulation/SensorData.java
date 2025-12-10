package com.example.server.Alerts.simulation;

import java.time.LocalDateTime;


public record SensorData(
        String source,
        String metric,
        double value,
        LocalDateTime timestamp
) {}