package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DataFetcher {

    private final JdbcTemplate jdbcTemplate;

    // przełącznik mockowania
    @Getter
    private final boolean mockEnabled = true;

    public List<DataPoint> fetchData(LocalDateTime from, LocalDateTime to, DataType type) {
        if (!mockEnabled) {
            // docelowa ścieżka pod bazę danych (na przyszłość). Na teraz sygnalizujemy brak implementacji.
            throw new UnsupportedOperationException("Tryb mock=false jest obecnie niezaimplementowany. Włącz mockowanie.");
        }

        // generujemy dane w pełni niezależnie od modułu symulacji.
        List<DataPoint> points = new ArrayList<>();
        LocalDateTime current = from;

        long seed = Objects.hash(from, to, type);
        Random random = new Random(seed);

        //bazowe wartości zależne od dnia roku (deterministycznie)
        int dayOfYear = from.getDayOfYear();
        double baseTemp = 5 + 15 * Math.sin((dayOfYear / 365.0) * 2 * Math.PI);
        double baseWind = 1.0 + 2.0 * Math.abs(Math.cos((dayOfYear / 365.0) * 2 * Math.PI));
        double baseInsol = 50 + 100 * Math.max(0, Math.sin((dayOfYear / 365.0) * 2 * Math.PI));

        while (current.isBefore(to)) {
            int hour = current.getHour();

            // produkcja tylko w dzień
            double daylightFactor;
            if (hour >= 6 && hour <= 18) {
                daylightFactor = 1.0 - (Math.abs(12 - hour) / 6.0);
                daylightFactor = Math.max(0, daylightFactor);
            } else {
                daylightFactor = 0.0;
            }

            // konsumpcja – wyższa rano i wieczorem
            double consumptionFactor;
            if (hour >= 6 && hour < 9) consumptionFactor = 1.2;
            else if (hour >= 17 && hour < 22) consumptionFactor = 1.35;
            else if (hour >= 0 && hour < 5) consumptionFactor = 0.7;
            else consumptionFactor = 1.0;

            // szum losowy w celu zmniejszenia wystąpienia patternu
            double noise = (random.nextDouble() - 0.5) * 2.0;

            double value;
            switch (type) {
                case ENERGY_CONSUMPTION -> {
                    double raw = 5.0 * (45.0 - baseTemp) * 0.02;
                    value = Math.max(0.0, (raw * consumptionFactor) + 8 + noise);
                }
                case ENERGY_PRODUCTION -> {
                    double raw = 0.12 * baseInsol + 1.8 * baseWind;
                    value = Math.max(0.0, raw * daylightFactor + noise * 0.5);
                }
                case DEVICE_EFFICIENCY -> {
                    double cons = Math.max(0.1, 5.0 * (45.0 - baseTemp) * 0.02 * consumptionFactor + 8);
                    double prod = Math.max(0.0, (0.12 * baseInsol + 1.8 * baseWind) * daylightFactor);
                    double eff = prod / cons;
                    value = Math.max(0.0, Math.min(1.0, eff + noise * 0.02));
                }
                default -> value = 10 + random.nextDouble() * 20;
            }

            points.add(new DataPoint(current, value, type, "simulation-device-1"));
            current = current.plusHours(1);
        }

        return points;
    }
}