package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.data.UsageRecord;
import lombok.Getter;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.regression.RandomForest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ForecasterModel {
    // nazwy kolumn w DataFrame
    private static final String COL_HOUR = "hour";
    private static final String COL_DAY = "day";
    private static final String COL_LAG1 = "lag1";
    private static final String COL_LAG24 = "lag24";
    private static final String COL_TARGET = "value";

    private static class HourlyRecord {
        LocalDateTime time;
        double value;

        public HourlyRecord(LocalDateTime time, double value) {
            this.time = time;
            this.value = value;
        }
    }

    private RandomForest model;

    private smile.data.type.StructType featureSchema;

    @Getter
    private Instant lastTrainingTime = null;


    public void trainModel(List<UsageRecord> rawHistory5Min) {
        List<HourlyRecord> hourlyHistory = aggregateToHourly(rawHistory5Min);

        if (hourlyHistory.size() < 25) {
            throw new IllegalArgumentException("Za mało danych (min. 25h): " + hourlyHistory.size());
        }

        int n = hourlyHistory.size() - 24;
        double[][] dataMatrix = new double[n][5]; // 4 cechy + 1 cel

        for (int i = 0; i < n; i++) {
            HourlyRecord target = hourlyHistory.get(i + 24);
            HourlyRecord prevHour = hourlyHistory.get(i + 24 - 1);
            HourlyRecord prevDay = hourlyHistory.get(i);

            dataMatrix[i][0] = target.time.getHour();              // hour
            dataMatrix[i][1] = target.time.getDayOfWeek().getValue(); // day
            dataMatrix[i][2] = prevHour.value;                     // lag1
            dataMatrix[i][3] = prevDay.value;                      // lag24
            dataMatrix[i][4] = target.value;                       // target (value)
        }

        DataFrame df = DataFrame.of(dataMatrix, COL_HOUR, COL_DAY, COL_LAG1, COL_LAG24, COL_TARGET);

        Formula formula = Formula.lhs(COL_TARGET);

        this.model = RandomForest.fit(formula, df);

        this.featureSchema = formula.x(df).schema();

        lastTrainingTime = Instant.now();
    }


    public List<Forecast> forecastNext7Days(List<UsageRecord> recentHistoryRaw) {
        if (model == null) {
            throw new IllegalStateException("Najpierw wytrenuj model!");
        }

        List<HourlyRecord> context = aggregateToHourly(recentHistoryRaw);
        if (context.size() < 24) {
            throw new IllegalArgumentException("Potrzebne min. 24h historii.");
        }

        LinkedList<Double> historyBuffer = new LinkedList<>();
        for (int i = 24; i > 0; i--) {
            historyBuffer.add(context.get(context.size() - i).value);
        }

        LocalDateTime lastKnownTime = context.getLast().time;
        List<HourlyRecord> futureHourlyPredictions = new ArrayList<>();
        LocalDateTime currentTime = lastKnownTime;
        double lastValue = historyBuffer.getLast();

        for (int i = 0; i < 7 * 24; i++) {
            currentTime = currentTime.plusHours(1);

            double[] featuresValues = new double[]{
                    (double) currentTime.getHour(),              // hour
                    (double) currentTime.getDayOfWeek().getValue(), // day
                    lastValue,                                   // lag1
                    historyBuffer.getFirst()                     // lag24
            };

            Tuple inputTuple = Tuple.of(this.featureSchema, featuresValues);

            double prediction = model.predict(inputTuple);
            if (prediction < 0) prediction = 0;

            futureHourlyPredictions.add(new HourlyRecord(currentTime, prediction));

            historyBuffer.add(prediction);
            historyBuffer.removeFirst();
            lastValue = prediction;
        }

        return calculateDailyAverages(futureHourlyPredictions);
    }

    private List<HourlyRecord> aggregateToHourly(List<UsageRecord> raw) {
        Map<LocalDateTime, List<Double>> grouped = raw.stream()
                .collect(Collectors.groupingBy(
                        r -> r.timestamp.truncatedTo(ChronoUnit.HOURS),
                        Collectors.mapping(r -> r.value, Collectors.toList())
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    double avg = entry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                    return new HourlyRecord(entry.getKey(), avg);
                })
                .sorted(Comparator.comparing(r -> r.time))
                .collect(Collectors.toList());
    }

    private List<Forecast> calculateDailyAverages(List<HourlyRecord> hourly) {
        Map<LocalDateTime, Double> dailyAverages = hourly.stream()
                .collect(Collectors.groupingBy(
                        r -> r.time.truncatedTo(ChronoUnit.DAYS),
                        Collectors.averagingDouble(r -> r.value)
                ));

        return dailyAverages.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new Forecast(e.getValue(), e.getKey()))
                .collect(Collectors.toList());
    }

    public static List<UsageRecord> generateMockUsageData(int days) {
        List<UsageRecord> data = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now().minusDays(days);

        Random random = new Random();
        int totalPoints = days * 24 * 12;

        for (int i = 0; i < totalPoints; i++) {
            LocalDateTime t = start.plusMinutes(i * 5L);

            double hourEffect = Math.sin((t.getHour() - 6) * Math.PI / 12);
            double baseValue = 10 + (hourEffect * 5);
            double noise = random.nextDouble() * 2;

            data.add(new UsageRecord(t, baseValue + noise));
        }

        return data;
    }
}
