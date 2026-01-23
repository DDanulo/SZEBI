package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import com.example.server.DataAnalysis.model.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DataFetcher {

    private final JdbcTemplate jdbcTemplate;

    public List<DataPoint> fetchData(LocalDateTime from, LocalDateTime to, DataType type) {
        return switch (type) {
            case ENERGY_CONSUMPTION -> queryMeasure(from, to, "consumed", DataType.ENERGY_CONSUMPTION);
            case ENERGY_PRODUCTION -> queryMeasure(from, to, "generated", DataType.ENERGY_PRODUCTION);
            case DEVICE_EFFICIENCY -> computeEfficiency(from, to);
        };
    }

    private List<DataPoint> queryMeasure(LocalDateTime from, LocalDateTime to, String discriminator, DataType targetType) {
        String sql = "SELECT timestamp, value, device_id FROM energy_measure WHERE timestamp BETWEEN ? AND ? AND type = ? ORDER BY timestamp";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LocalDateTime ts = rs.getTimestamp("timestamp").toLocalDateTime();
            double value = rs.getBigDecimal("value") != null ? rs.getBigDecimal("value").doubleValue() : 0.0;
            UUID deviceId = (UUID) rs.getObject("device_id");
            return new DataPoint(ts, value, targetType, deviceId != null ? deviceId.toString() : null);
        }, from, to, discriminator);
    }

    private List<DataPoint> computeEfficiency(LocalDateTime from, LocalDateTime to) {
        List<DataPoint> cons = queryMeasure(from, to, "consumed", DataType.ENERGY_CONSUMPTION);
        List<DataPoint> prod = queryMeasure(from, to, "generated", DataType.ENERGY_PRODUCTION);

        Map<LocalDateTime, Double> totalConsByTs = new HashMap<>();
        for (DataPoint dp : cons) {
            totalConsByTs.merge(dp.getTimestamp(), dp.getValue(), Double::sum);
        }

        Map<LocalDateTime, Double> totalProdByTs = new HashMap<>();
        for (DataPoint dp : prod) {
            totalProdByTs.merge(dp.getTimestamp(), dp.getValue(), Double::sum);
        }

        List<DataPoint> result = new ArrayList<>();

        for (Map.Entry<LocalDateTime, Double> entry : totalConsByTs.entrySet()) {
            LocalDateTime ts = entry.getKey();
            double totalCons = entry.getValue();
            double totalProd = totalProdByTs.getOrDefault(ts, 0.0);

            double efficiency = 0.0;
            if (totalCons > 0) {
                efficiency = Math.min(1.0, totalProd / totalCons);
            }

            result.add(new DataPoint(ts, efficiency, DataType.DEVICE_EFFICIENCY, "SYSTEM"));
        }

        result.sort(Comparator.comparing(DataPoint::getTimestamp));
        return result;
    }
}