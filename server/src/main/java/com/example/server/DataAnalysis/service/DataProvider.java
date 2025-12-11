package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.api.IData;
import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
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

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
