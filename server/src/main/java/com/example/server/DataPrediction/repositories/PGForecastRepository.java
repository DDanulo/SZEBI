//package com.example.server.DataPrediction.repositories;
//
//import com.example.server.DataPrediction.data.Forecast;
//import jakarta.persistence.EntityTransaction;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class PGForecastRepository {
//    private final JdbcTemplate jdbcTemplate;
//
//    public PGForecastRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public List<Double> fetchTemperatures(Instant from, Instant to) {
//        return List.of();
//    }
//
//    @Override
//    public List<Integer> fetchUsages(Instant from, Instant to) {
//        return List.of();
//    }
//
//    @Override
//    public List<Forecast> getForecasts(List<UUID> forecastIds) {
//        return List.of();
//    }
//
//    @Override
//    public void saveForecast(Forecast forecast) {
//    }
//}
