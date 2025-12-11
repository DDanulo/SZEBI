package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.repositories.ForecastRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final ForecastRepository forecastRepository;
    private Thread generationThread;
    private int generationInterval; // w minutach


    public ForecastServiceImpl(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
        this.generationInterval = 60;
//        createGenerationThread();
        generateInitForecasts(10);
    }

    private void createGenerationThread() {
        generationThread = new Thread(() -> {
            while (true) {
                generateForecasts();
                try {
                    sleep((long) generationInterval * 60 * 1000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        generationThread.start();
    }

    @Override
    public void changeInterval(int generationInterval) {
        this.generationInterval = generationInterval;
        generationThread.interrupt();
        createGenerationThread();
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

    @Override
    public void generateForecast() {
        generateForecasts();
    }

    /**
     * Tworzenie prognozy zużycia energii po zmianie modelu danych przez Moduł Symulacji
     *
     * @param previousUsage lista zużycia energii elektrycznej z poprzednich 7 dni
     * @param temperatures  lista temperatur na nastepne <code>forDay</code> dni
     * @param forDay        prognoza, na jaki kolejny dzień
     */
    private void createForecast(List<Integer> previousUsage, List<Double> temperatures, long forDay) {
        int averageUsage = getAverageInteger(previousUsage);
        double averageTemperature = getAverageDouble(temperatures);

        int forecastedUsage = averageUsage + (int) Math.floor(-averageTemperature) + 40;

        Forecast forecast = new Forecast(forecastedUsage, LocalDateTime.now().plusDays(forDay));

        forecastRepository.save(forecast);
    }

    /**
     * Generuje i zapisuje do bazy <code>DAY_INTERVAL</code> prognoz
     */
    private void generateForecasts() {
        final int DAY_INTERVAL = 7;

        List<Integer> previousUsage = forecastRepository.fetchUsages(
                LocalDateTime.now().minusDays(DAY_INTERVAL), LocalDateTime.now());

        for (int d = 1; d <= DAY_INTERVAL; d++) {
            List<Double> temperatures = Arrays.asList(1.0, 2.0, 0.0, 0.0, 3.0, 5.0, 10.0);

            createForecast(previousUsage, temperatures, d);
        }
    }

    private int getAverageInteger(List<Integer> values) {
        int totalUsage = 0;
        for (int usage : values) {
            totalUsage += usage;
        }
        return !values.isEmpty() ? totalUsage / values.size() : 0;
    }

    private double getAverageDouble(List<Double> values) {
        double totalUsage = 0;
        for (double usage : values) {
            totalUsage += usage;
        }
        return totalUsage / values.size();
    }

    private void generateInitForecasts(int quantity) {
        List<Integer> usage = Arrays.asList(10, 11, 13, 11, 15, 10, 9);
        List<Double> temperatures = Arrays.asList(1.0, 2.0, 0.0, 0.0, 3.0, 5.0, 10.0);

        for (int i = 0; i < quantity; i++) {
            createForecast(usage, temperatures, i);
        }
    }
}
