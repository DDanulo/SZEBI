package com.example.server.DataAnalysis.api;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IData {

    List<DataPoint> getRawData(LocalDateTime from, LocalDateTime to, DataType type);

    Map<String, Double> getAggregatedData(LocalDateTime from, LocalDateTime to, DataType type);

    LocalDateTime getLastUpdated();
}
