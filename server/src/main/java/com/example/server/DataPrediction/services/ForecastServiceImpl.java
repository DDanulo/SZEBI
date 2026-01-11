package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.data.UsageRecord;
import com.example.server.DataPrediction.exceptions.ModelNotTrainedException;
import com.example.server.DataPrediction.repositories.ForecastRepository;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class ForecastServiceImpl implements ForecastService {
    private final ForecastRepository forecastRepository;
    private final ForecasterModel model;
    private boolean useMockData = false; // do testowania

    public ForecastServiceImpl(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
        model = new ForecasterModel();
    }

    @Override
    @Transactional(readOnly = true)
    public Forecast getLatestForecast() {
        return forecastRepository.findTopByOrderByCreationTimeDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forecast> getForecasts(Instant from, Instant to) {
        return forecastRepository.findAllByCreationTimeIsBetween(from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forecast> getLatestForecasts() {
        return forecastRepository.findLatestForecasts();
    }

    @Override
    public void generateForecast() throws ModelNotTrainedException {
        generateDailyForecasts();
    }

    @Override
    public void switchMockMode() {
        useMockData = true;
        trainModel();
        generateForecast();
    }

    /**
     * Tworzenie prognozy zużycia energii i zapisanie prognoz do bazy danych.
     * W przypadku wygenerowania prognozy gdzie wartości są "od czapy", model jest trenowany ponownie i jeszcze raz
     * generuje prognozę, ale przy drugim podejściu zapisuje ją do bazy niezależnie od sensowności wyniku.
     * @param previousUsage lista zużycia energii elektrycznej z poprzednich 7 dni
     * @param secondChance  sprawdzenie, czy jest to drugie podejście
     */
    private void createForecast(List<UsageRecord> previousUsage, boolean secondChance) {
        List<Forecast> forecasts = model.forecastNext7Days(previousUsage);

        boolean isGood = true;

        for (Forecast forecast : forecasts) {
            if (forecast.getForecastedUsage() > 1000 || forecast.getForecastedUsage() <= 0) {
                isGood = false;
                break;
            }
        }

        if (isGood || secondChance) {
            forecastRepository.saveAll(forecasts);
        } else {
            trainModel();
            createForecast(previousUsage, true);
        }
    }

    /**
     * Overload do powyższej metody, bo Java nie ma defaultowych parametrów
     * @param previousUsage poprzednie zużycie
     */
    private void createForecast(List<UsageRecord> previousUsage) {
        createForecast(previousUsage, false);
    }

    /**
     * Trenowanie algorytmu na wcześniejszych danych sprzed maksymalnie 14 dni.
     * Trening odbywa się okresowo co tydzień. Pierwszy trening jest z opóźnieniem 5 sekund, żeby jakieś dane były w bazie
     */
    @Scheduled(initialDelay = 5, fixedDelay = 604800, timeUnit = TimeUnit.SECONDS)
    void trainModel() {
        List<UsageRecord> trainData;

        if (useMockData) {
            trainData = ForecasterModel.generateMockUsageData(14);
        } else {
            trainData = forecastRepository.fetchUsages(LocalDateTime.now().minusDays(14L), LocalDateTime.now());
        }

        try {
            model.trainModel(trainData);
        } catch (IllegalArgumentException e) {
            System.err.println("[ForecastServiceImpl] Model nie mógł zostać wytrenowany: " + e.getMessage());
        }
    }

    /**
     * Generuje i zapisuje do bazy 8 prognoz (7 pełnych dób) co <code>fixedDelay</code> minut.
     * Zaczyna generować <code>initialDelay</code> minut po utworzeniu obiektu.
     *
     * @throws ModelNotTrainedException w przypadku, gdy model jeszcze nie został wytrenowany
     */
    @Scheduled(initialDelay = 1, fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    void generateDailyForecasts() throws ModelNotTrainedException {
        List<UsageRecord> previousUsage;

        if (useMockData) {
            previousUsage = ForecasterModel.generateMockUsageData(7);
        } else {
            previousUsage = forecastRepository.fetchUsages(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        }

        try {
            createForecast(previousUsage);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("[ForecastServiceImpl] Prognoza nie mogła zostać wygenerowana: " + e.getMessage());
            if (e.getClass().equals(IllegalStateException.class)) {
                throw new ModelNotTrainedException("Model nie został jeszcze wytrenowany.", e);
            }
        }
    }
}
