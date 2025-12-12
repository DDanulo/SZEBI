package com.example.server.DataPrediction.controllers;

import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/prediction")
@CrossOrigin(origins = "http://localhost:5173")
public class DataPredictionController {
    private final ForecastService forecastService;

    public DataPredictionController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Forecast> getForecast(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(forecastService.getForecast(UUID.fromString(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<Forecast> getLatestForecast() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(forecastService.getLatestForecast());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{interval}")
    public ResponseEntity<String> updateInterval(@PathVariable String interval) {
        try {
            forecastService.changeInterval(Integer.parseInt(interval));
            return ResponseEntity.status(HttpStatus.OK).body("Generation interval has been set to: " + interval + " minutes");
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/generate")
    public ResponseEntity<String> generateForecasts() {
        forecastService.generateForecast();
        return ResponseEntity.created(URI.create("http://localhost:8080/prediction/latest")).build();
    }
}
