package com.example.server.DataAnalysis.service;

import com.example.server.DataAnalysis.model.DataPoint;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public Map<String, Double> aggregateByHour(List<DataPoint> data) {
        return data.stream().collect(Collectors.groupingBy(
                dp -> dp.getTimestamp().truncatedTo(ChronoUnit.HOURS).toString(),
                Collectors.summingDouble(DataPoint::getValue))
        );
    }

    public Map<String, Double> aggregateByDay(List<DataPoint> data) {
        return data.stream().collect(Collectors.groupingBy(
                dp -> dp.getTimestamp().toLocalDate().toString(),
                Collectors.summingDouble(DataPoint::getValue)
        ));
    }

    public Map<String, Double> aggregateByWeek(List<DataPoint> data) {
        WeekFields wf = WeekFields.ISO;

        return data.stream().collect(Collectors.groupingBy(
                dp -> {
                    int year = dp.getTimestamp().get(wf.weekBasedYear());
                    int week = dp.getTimestamp().get(wf.weekOfWeekBasedYear());
                    return String.format("%d-W%02d", year, week);
                },
                TreeMap::new,
                Collectors.summingDouble(DataPoint::getValue)
        ));
    }

    public Map<String, Double> aggregateByMonth(List<DataPoint> data) {
        return data.stream().collect(Collectors.groupingBy(
                dp -> YearMonth.from(dp.getTimestamp()).toString(),
                Collectors.summingDouble(DataPoint::getValue)
        ));
    }

    public Map<String, Double> aggregateByYear(List<DataPoint> data) {
        return data.stream().collect(Collectors.groupingBy(
                dp -> Year.from(dp.getTimestamp()).toString(),
                Collectors.summingDouble(DataPoint::getValue)
        ));
    }
}