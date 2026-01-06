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
    // Nazwy kolumn w DataFrame (ważne dla mapowania)
    private static final String COL_HOUR = "hour";
    private static final String COL_DAY = "day";
    private static final String COL_LAG1 = "lag1";
    private static final String COL_LAG24 = "lag24";
    private static final String COL_TARGET = "value";

    // --- PROSTE KLASY DANYCH (POJO) ---
    // (Bez zmian w stosunku do poprzedniej wersji)

    private static class HourlyRecord {
        LocalDateTime time;
        double value;

        public HourlyRecord(LocalDateTime time, double value) {
            this.time = time;
            this.value = value;
        }
    }

    // --- GŁÓWNA LOGIKA ---

    private RandomForest model;

    // Schema danych wejściowych (bez targetu) - potrzebny do tworzenia Tuple przy predykcji
    private smile.data.type.StructType featureSchema;

    @Getter
    private Instant lastTrainingTime = null;

    /**
     * KROK 1: TRENING MODELU (API SMILE 3.0+)
     */
    public void trainModel(List<UsageRecord> rawHistory5Min) {
        List<HourlyRecord> hourlyHistory = aggregateToHourly(rawHistory5Min);

        if (hourlyHistory.size() < 25) {
            throw new IllegalArgumentException("Za mało danych (min. 25h): " + hourlyHistory.size());
        }

        // Przygotowanie danych do DataFrame
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

        // 1. Tworzymy DataFrame (nazywamy kolumny)
        DataFrame df = DataFrame.of(dataMatrix, COL_HOUR, COL_DAY, COL_LAG1, COL_LAG24, COL_TARGET);

        // 2. Definiujemy Formułę: "value ~ ." (Przewiduj 'value' używając wszystkich innych kolumn)
        Formula formula = Formula.lhs(COL_TARGET);

        // 3. Opcje modelu (RandomForest.Options)
        // W nowym Smile można użyć domyślnej metody fit, która używa domyślnych opcji,
        // lub skonfigurować własne. Tutaj użyjemy przeciążenia z domyślnymi opcjami dla prostoty.
        System.out.println("Trening modelu...");

        // fit(Formula, DataFrame) - to jest kluczowa zmiana w API
        this.model = RandomForest.fit(formula, df);

        // Zapisujemy schemat cech (potrzebny do predykcji) - pobieramy schemat X (bez Y)
        this.featureSchema = formula.x(df).schema();

        System.out.println("Model gotowy!");
        lastTrainingTime = Instant.now();
    }

    /**
     * KROK 2: PROGNOZOWANIE (API SMILE 3.0+)
     */
    public List<Forecast> forecastNext7Days(List<UsageRecord> recentHistoryRaw) {
        if (model == null) {
            throw new IllegalStateException("Najpierw wytrenuj model!");
        }

        List<HourlyRecord> context = aggregateToHourly(recentHistoryRaw);
        if (context.size() < 24) {
            throw new IllegalArgumentException("Potrzebne min. 24h historii.");
        }

        // Bufor na historię (tak jak poprzednio)
        LinkedList<Double> historyBuffer = new LinkedList<>();
        for (int i = 24; i > 0; i--) {
            historyBuffer.add(context.get(context.size() - i).value);
        }

        LocalDateTime lastKnownTime = context.getLast().time;
        List<HourlyRecord> futureHourlyPredictions = new ArrayList<>();
        LocalDateTime currentTime = lastKnownTime;
        double lastValue = historyBuffer.getLast();

        // Pętla na 7 dni (168 godzin)
        for (int i = 0; i < 7 * 24; i++) {
            currentTime = currentTime.plusHours(1);

            // Budujemy surowy wiersz danych (tylko cechy, bez targetu)
            double[] featuresValues = new double[]{
                    (double) currentTime.getHour(),              // hour
                    (double) currentTime.getDayOfWeek().getValue(), // day
                    lastValue,                                   // lag1
                    historyBuffer.getFirst()                     // lag24
            };

            // TWORZENIE TUPLE (Wymagane przez Smile 3.0+ dla predict())
            // Najprostszy sposób to utworzenie jednowierszowego DataFrame lub użycie Tuple.of
            // Tuple.of wymaga podania tablicy wartości i schematu (struktury)
            Tuple inputTuple = Tuple.of(this.featureSchema, featuresValues);

            // Wywołanie modelu
            double prediction = model.predict(inputTuple);
            if (prediction < 0) prediction = 0;

            futureHourlyPredictions.add(new HourlyRecord(currentTime, prediction));

            // Aktualizacja buforów
            historyBuffer.add(prediction);
            historyBuffer.removeFirst();
            lastValue = prediction;
        }

        return calculateDailyAverages(futureHourlyPredictions);
    }

    // --- METODY POMOCNICZE (Bez zmian logicznych) ---

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
        int totalPoints = days * 24 * 12; // co 5 minut

        for (int i = 0; i < totalPoints; i++) {
            LocalDateTime t = start.plusMinutes(i * 5L);
            // Prosty wzór: więcej w dzień (godz 12), mniej w nocy + trochę losowości
            double hourEffect = Math.sin((t.getHour() - 6) * Math.PI / 12);
            double baseValue = 10 + (hourEffect * 5);
            double noise = random.nextDouble() * 2;

            data.add(new UsageRecord(t, baseValue + noise));
            System.out.println(data.getLast());
        }

        return data;
    }
}
