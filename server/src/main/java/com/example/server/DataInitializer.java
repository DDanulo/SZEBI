package com.example.server;

import com.example.server.DeviceControl.manager.ManagerAppliance;
import com.example.server.Simulation.data.ConsumingDeviceRepository;
import com.example.server.Simulation.data.ConsumingMeasureRepository;
import com.example.server.Simulation.entities.ConsumedEnergyMeasure;
import com.example.server.Simulation.entities.ConsumingDevice;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
class DataInitializer implements ApplicationRunner {

    private final ConsumingMeasureRepository measureRepository;
    private final ConsumingDeviceRepository deviceRepository;

    private final ManagerAppliance deviceManager;

    @Override
    public void run(ApplicationArguments args) {
        if (measureRepository.count() > 4000){
            return;
        }
        System.out.println("Inicjalizacja danych zużycia energii elektrycznej sprzed 15 dni...");

        String name = "SigmaBoy";
        System.out.println("Dodawanie urządzenia: " + name);
        deviceManager.addDevice(name, 67.0, 2137, 420, UUID.randomUUID());
        System.out.println("Dodano urządzenie: " + deviceManager.getDevices().getFirst().getName());

        int days = 15;
        System.out.println("\nGenerowanie danych zużycia energii elektrycznej sprzed " + days + " dni...");
        List<ConsumedEnergyMeasure> consumption = generateHistory(deviceRepository.findByWorkingTrue().getFirst(), days);
        System.out.println("\nWygenerowano " + consumption.size() +
                " rekordów danych zużycia energii elektrycznej sprzed " + days + " dni...");
        measureRepository.saveAll(consumption);
        System.out.println("\nZapisano do repozytorium " + measureRepository.count() +
                " rekordów danych zużycia energii elektrycznej sprzed " + days + " dni...");
    }

    private List<ConsumedEnergyMeasure> generateHistory(ConsumingDevice device, int daysBack) {
        List<ConsumedEnergyMeasure> data = new ArrayList<>();

        LocalDateTime endDate = LocalDateTime.now();

        LocalDateTime current = endDate.minusDays(daysBack);

        while (current.isBefore(endDate)) {

            double temp = generateTemperature(current);

            ConsumedEnergyMeasure measure = new ConsumedEnergyMeasure(current, device.consumeEnergy(temp), device);

            data.add(measure);

            current = current.plusMinutes(5);
        }

        return data;
    }

    private double generateTemperature(LocalDateTime dateTime) {
        double avgTemp = 10.0;
        double yearlySwing = 15.0;
        double dailySwing = 5.0;

        double seasonalAngle = ((dateTime.getDayOfYear() - 20) / 365.0) * 2 * Math.PI;
        double seasonalEffect = -Math.cos(seasonalAngle) * yearlySwing;

        double hour = dateTime.getHour() + (dateTime.getMinute() / 60.0);
        double dailyAngle = ((hour - 14) / 24.0) * 2 * Math.PI;
        double dailyEffect = Math.cos(dailyAngle) * dailySwing;

        double noise = (Math.random() * 4.0) - 2.0;

        double result = avgTemp + seasonalEffect + dailyEffect + noise;

        return Math.round(result * 10.0) / 10.0;
    }
}

