package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataProcessor {

    public double calculateAverage(List<DataPoint> data) {
        if (data.isEmpty()) return 0.0;
        return data.stream().mapToDouble(DataPoint::getValue).average().orElse(0.0);
    }

    public double calculateTotal(List<DataPoint> data) {
        return data.stream().mapToDouble(DataPoint::getValue).sum();
    }

    public Map<String, Double> aggregateByDay(List<DataPoint> data) {
        return data.stream().collect(Collectors.groupingBy(
                dp -> dp.getTimestamp().toLocalDate().toString(),
                Collectors.summingDouble(DataPoint::getValue)
        ));
    }
}