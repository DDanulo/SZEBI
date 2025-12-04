package com.example.server.DataPrediction.repositories;

import com.example.server.DataPrediction.data.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, UUID> {
    @Query(value = "SELECT * FROM temperatures WHERE date BETWEEN :from AND :to ", nativeQuery = true)
    List<Double> fetchTemperatures(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT * FROM usages WHERE date BETWEEN :from AND :to ", nativeQuery = true)
    List<Integer> fetchUsages(LocalDateTime from, LocalDateTime to);
}
