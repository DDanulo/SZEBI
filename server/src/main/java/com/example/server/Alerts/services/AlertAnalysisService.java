package com.example.server.Alerts.services;

import com.example.server.Alerts.entities.Metric;
import com.example.server.Alerts.simulation.SensorData;
import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import com.example.server.DataAnalysis.service.DataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertAnalysisService {

    private final DataFetcher dataFetcher;
    private final AlertDataInput alertDataInput;

    private final Map<Metric, LocalDateTime> lastProcessedMap = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 10000, fixedRate = 20000)
    public void analyzeIncomingData() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fetchWindowStart = now.minusMinutes(2);

        for (DataType type : DataType.values()) {
            try {

                Metric metric = mapToMetric(type);
                if (metric == null) continue;


                List<DataPoint> rawData = dataFetcher.fetchData(fetchWindowStart, now, type);

                if (rawData.isEmpty()) continue;


                LocalDateTime lastProcessedTime = lastProcessedMap.getOrDefault(metric, fetchWindowStart);


                List<DataPoint> newDataPoints = rawData.stream()
                        .filter(point -> point.getTimestamp().isAfter(lastProcessedTime))
                        .sorted(Comparator.comparing(DataPoint::getTimestamp))
                        .toList();

                if (newDataPoints.isEmpty()) {
                    continue;
                }


                for (DataPoint point : newDataPoints) {
                    processSinglePoint(point, metric);
                }

                DataPoint newestPoint = newDataPoints.get(newDataPoints.size() - 1);
                lastProcessedMap.put(metric, newestPoint.getTimestamp());

            } catch (Exception e) {
                //log.error("Błąd przy przetwarzaniu typu {}: ", type, e);
            }
        }
    }

    private void processSinglePoint(DataPoint point, Metric metric) {
        SensorData sensorData = SensorData.builder()
                .source("Moduł Analizy")
                .metric(metric)
                .value(point.getValue())
                .timestamp(point.getTimestamp())
                .userID(null)
                .location(null)
                .build();

        alertDataInput.handleData(sensorData);
    }

    private Metric mapToMetric(DataType dataType) {
        try {
            return Metric.valueOf(dataType.name());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}