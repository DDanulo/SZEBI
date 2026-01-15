package com.example.server.DataPrediction.controllers;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/predictions")
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

    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @GetMapping("/latest/batch")
    public ResponseEntity<List<Forecast>> getLatestForecasts() {
        return ResponseEntity.status(HttpStatus.OK).body(forecastService.getLatestForecasts());
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @GetMapping(value = "/forecasts/{from}/{to}")
    public ResponseEntity<List<Forecast>> getForecasts(@PathVariable long from, @PathVariable long to) {
        Instant fromTime = Instant.ofEpochMilli(from);
        Instant toTime = Instant.ofEpochMilli(to);
        return ResponseEntity.status(HttpStatus.OK).body(forecastService.getForecasts(fromTime, toTime));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @PutMapping("/generate")
    public ResponseEntity<String> generateForecasts() {
        forecastService.generateForecast();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ENGINEER')")
    @GetMapping(value = "/mockitbro")
    public ResponseEntity<String> mockitbro() {
        forecastService.switchMockMode();
        return ResponseEntity.status(HttpStatus.OK).body("Bro got mocked");
    }

}
