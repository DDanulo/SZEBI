package com.example.server.DataPrediction.repositories;

import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.data.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, UUID> {

//    @Query("SELECT new com.example.server.DataPrediction.data.UsageRecord(e.timestamp, e.value) " +
//            "FROM EnergyMeasure e " +
//            "WHERE (e.timestamp >= :from AND e.timestamp <= :to) AND e.type = 'consumed' " +
//            "ORDER BY e.timestamp ASC")
//    List<UsageRecord> fetchUsages(@Param("from") LocalDateTime from,  @Param("to") LocalDateTime to);

    @Query("SELECT new com.example.server.DataPrediction.data.UsageRecord(e.timestamp, e.value) " +
            "FROM EnergyMeasure e " +
            "WHERE (e.timestamp >= :from AND e.timestamp <= :to) " +
            "ORDER BY e.timestamp ASC")
    List<UsageRecord> fetchUsages(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    List<Forecast> findAllByCreationTimeIsBetween(Instant creationTimeAfter, Instant creationTimeBefore);

    Forecast findTopByOrderByCreationTimeDesc();

    @Query("SELECT f FROM Forecast f WHERE f.creationTime = (SELECT MAX(f2.creationTime) FROM Forecast f2)")
    List<Forecast> findLatestForecasts();
}
