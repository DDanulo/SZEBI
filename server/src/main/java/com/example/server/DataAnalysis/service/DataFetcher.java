package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataFetcher {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Pobiera dane historyczne z bazy danych (tabela usages - symulacja)
     */
    public List<DataPoint> fetchData(LocalDateTime from, LocalDateTime to, DataType type) {
        String sql = "SELECT * FROM usages WHERE date BETWEEN ? AND ?";

        List<DataPoint> points = new ArrayList<>();
        LocalDateTime current = from;
        while (current.isBefore(to)) {
            double value = 10 + Math.random() * 20;
            points.add(new DataPoint(current, value, type, "simulation-device-1"));
            current = current.plusHours(1);
        }

        return points;
    }
}