package com.example.server.DataPrediction.api;

import com.example.server.DataPrediction.data.Forecast;

import java.util.UUID;

public interface ForecastService {
    void changeInterval(int generationInterval);

    Forecast getLatestForecast();

    Forecast getForecast(UUID forecastId);

    void generateForecast();
}
