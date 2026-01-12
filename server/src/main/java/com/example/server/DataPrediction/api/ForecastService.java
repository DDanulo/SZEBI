package com.example.server.DataPrediction.api;

import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.exceptions.ModelNotTrainedException;

import java.time.Instant;
import java.util.List;

public interface ForecastService {
    Forecast getLatestForecast();

    List<Forecast> getLatestForecasts();

    List<Forecast> getForecasts(Instant from, Instant to);

    void generateForecast() throws ModelNotTrainedException;

    void switchMockMode();
}
