package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.data.Forecast;
import com.example.server.DataPrediction.data.UsageRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ForecasterModelTest {
    private static List<UsageRecord> data;
    private static double minUsage = Double.MAX_VALUE;
    private static double maxUsage = Double.MIN_VALUE;

    // Generator sztucznych danych do testów (sinusoida + szum)
    @BeforeAll
    public static void SetUp() {
        data = ForecasterModel.generateMockUsageData(14);

        for (UsageRecord usageRecord : data) {
            double usage = usageRecord.value;

            if (usage < minUsage) {
                minUsage = usage;
            } else if (usage > maxUsage) {
                maxUsage = usage;
            }
        }
    }

    @Test
    public void PredictionTest() {
        ForecasterModel model = new ForecasterModel();

        // 1. SYMULACJA DANYCH HISTORYCZNYCH (np. z 2 tygodni)
        // Generujemy sztuczne dane, żeby model miał na czym się uczyć
        List<UsageRecord> mockHistory = data;

        // 2. TRENING (normalnie trwałoby to chwilę)
        model.trainModel(mockHistory);

        assertNotNull(model.getLastTrainingTime());
        assertTrue(model.getLastTrainingTime().isBefore(Instant.now()));

        // 3. PROGNOZA (symulacja "tu i teraz")
        // Pobieramy "ostatnie" dane z bazy (np. z ostatnich 2 dni)
        List<UsageRecord> recentData = mockHistory.subList(
                mockHistory.size() - (2 * 24 * 12), // ostatnie 2 dni (12 pomiarów na godzinę)
                mockHistory.size()
        );

        System.out.println("Generowanie prognozy na 7 dni...");
        List<Forecast> forecasts = model.forecastNext7Days(recentData);

        // 4. WYŚWIETLENIE WYNIKÓW
        System.out.println("--- WYNIKI ---");
        for (Forecast forecast : forecasts) {
            System.out.println(forecast);
            assertTrue(forecast.getForecastedUsage() >= minUsage);
            assertTrue(forecast.getForecastedUsage() <= maxUsage);
        }

    }

    @Test
    public void PredictionTestFail() {
        ForecasterModel model = new ForecasterModel();

        assertThrows(IllegalStateException.class, () -> model.forecastNext7Days(null));
    }

    @Test
    public void TrainTestFail() {
        ForecasterModel model = new ForecasterModel();

        List<UsageRecord> mockHistory = data.subList(0, 20);

        assertThrows(IllegalArgumentException.class, () -> model.trainModel(mockHistory));
    }

}
