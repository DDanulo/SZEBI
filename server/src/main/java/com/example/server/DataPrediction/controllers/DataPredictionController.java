package com.example.server.DataPrediction.controllers;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/predictions")
@CrossOrigin(origins = "http://localhost:5173")
public class DataPredictionController {
    private final ForecastService forecastService;

    public DataPredictionController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/latest/one")
    public ResponseEntity<Forecast> getLatestForecast() {
        return ResponseEntity.status(HttpStatus.OK).body(forecastService.getLatestForecast());
    }

    @GetMapping("/latest/batch")
    public ResponseEntity<List<Forecast>> getLatestForecasts() {
        return ResponseEntity.status(HttpStatus.OK).body(forecastService.getLatestForecasts());
    }

    @GetMapping(value = "/forecasts", consumes = "application/json")
    public ResponseEntity<List<Forecast>> getForecasts(@RequestBody FromTo fromTo) {
        return ResponseEntity.status(HttpStatus.OK).body(forecastService.getForecasts(fromTo.from, fromTo.to));
    }

    @PutMapping("/generate")
    public ResponseEntity<String> generateForecasts() {
        forecastService.generateForecast();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/mockitbro")
    public ResponseEntity<String> mockitbro() {
        forecastService.switchMockMode();
        return ResponseEntity.status(HttpStatus.OK).body("Bro got mocked");
    }

    public record FromTo(
            Instant from,
            Instant to
    ) {
    }
}
