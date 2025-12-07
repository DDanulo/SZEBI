package com.example.server.DataPrediction.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "forecasts")
public class Forecast {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private Instant creationTime;
    private int forecastedUsage;
    private LocalDateTime forecastDate;

    public Forecast(int forecastedUsage, LocalDateTime forecastDate) {
        this.forecastedUsage = forecastedUsage;
        this.forecastDate = forecastDate;
        this.creationTime = Instant.now();
    }
}
