package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.repositories.ForecastRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final ForecastRepository forecastRepository;
    private final int dayInterval = 7;
    private int generationInterval;

    public ForecastServiceImpl(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
        this.generationInterval = 60;
    }

    @Override
    public void changeInterval(int generationInterval) {
        this.generationInterval = generationInterval;
    }

    @Override
    @Transactional(readOnly = true)
    public Forecast getLatestForecast() {
        return forecastRepository.findAll().getLast();
    }

    @Override
    @Transactional(readOnly = true)
    public Forecast getForecast(UUID forecastId) {
        return forecastRepository.findById(forecastId).orElse(null);
    }

    private void createForecast(List<Integer> previousUsage, List<Double> temperatures, long forDay) {
        int averageUsage = getAverageInteger(previousUsage);
        double averageTemperature = getAverageDouble(temperatures);

        int forecastedUsage = averageUsage + (int) Math.floor(-averageTemperature) + 40;

        Forecast forecast = new Forecast(forecastedUsage, LocalDateTime.now().plusDays(forDay));

        forecastRepository.save(forecast);
    }

    @Scheduled(fixedRate = 60000)
    private void generateForecasts() {
        List<Integer> previousUsage = forecastRepository.fetchUsages(
                LocalDateTime.now().minusDays(dayInterval), LocalDateTime.now());

        for (int d = 1; d <= dayInterval; d++) {
            List<Double> temperatures = forecastRepository.fetchTemperatures(
                    LocalDateTime.now(), LocalDateTime.now().plusDays(d));

            createForecast(previousUsage, temperatures, d);
        }
    }

    private int getAverageInteger(List<Integer> values) {
        int totalUsage = 0;
        for (int usage : values) {
            totalUsage += usage;
        }
        return totalUsage / values.size();
    }

    private double getAverageDouble(List<Double> values) {
        double totalUsage = 0;
        for (double usage : values) {
            totalUsage += usage;
        }
        return totalUsage / values.size();
    }
}
