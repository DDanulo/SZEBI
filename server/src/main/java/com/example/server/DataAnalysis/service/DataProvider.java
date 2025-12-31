package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.api.IData;
import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataProvider implements IData {

    private final DataFetcher dataFetcher;
    private final DataProcessor dataProcessor;

    @Getter
    private LocalDateTime lastUpdated;

    @Override
    public List<DataPoint> getRawData(LocalDateTime from, LocalDateTime to, DataType type) {
        List<DataPoint> data = dataFetcher.fetchData(from, to, type);
        lastUpdated = LocalDateTime.now();
        return data;
    }

    @Override
    public Map<String, Double> getAggregatedData(LocalDateTime from, LocalDateTime to, DataType type) {
        List<DataPoint> data = getRawData(from, to, type);
        return dataProcessor.aggregateByDay(data);
    }

    public double calculateAverage(List<DataPoint> data) {
        return dataProcessor.calculateAverage(data);
    }

    public double calculateTotal(List<DataPoint> data) {
        return dataProcessor.calculateTotal(data);
    }

    public Map<String, Double> aggregateByHour(List<DataPoint> data) {
        return dataProcessor.aggregateByHour(data);
    }

    public Map<String, Double> aggregateByDay(List<DataPoint> data) {
        return dataProcessor.aggregateByDay(data);
    }

    public Map<String, Double> aggregateByWeek(List<DataPoint> data) {
        return dataProcessor.aggregateByWeek(data);
    }

    public Map<String, Double> aggregateByMonth(List<DataPoint> data) {
        return dataProcessor.aggregateByMonth(data);
    }

    public Map<String, Double> aggregateByYear(List<DataPoint> data) {
        return dataProcessor.aggregateByYear(data);
    }
}
