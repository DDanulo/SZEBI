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
    private double forecastedUsage;
    private LocalDateTime forecastDate;

    public Forecast(Instant creationTime, double forecastedUsage, LocalDateTime forecastDate) {
        this.creationTime = creationTime;
        this.forecastedUsage = forecastedUsage;
        this.forecastDate = forecastDate;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "creationTime=" + creationTime +
                ", forecastedUsage=" + forecastedUsage +
                ", forecastDate=" + forecastDate +
                '}';
    }
}
