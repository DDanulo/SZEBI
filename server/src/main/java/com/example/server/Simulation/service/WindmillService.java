package com.example.server.Simulation.service;


import com.example.server.Simulation.data.GeneratedMeasureRepository;
import com.example.server.Simulation.data.WindmillRepository;
import com.example.server.Simulation.entities.EnergyProducingDevice;
import com.example.server.Simulation.entities.GeneratedEnergyMeasure;
import com.example.server.Simulation.entities.Windmill;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WindmillService {

    private final BasicSimulator simulator;


    private final WindmillRepository windmillRepository;

    private final GeneratedMeasureRepository generatedMeasureRepository;

    public WindmillService(BasicSimulator simulator, WindmillRepository windmillRepository, GeneratedMeasureRepository generatedMeasureRepository) {
        this.simulator = simulator;
        this.windmillRepository = windmillRepository;
        this.generatedMeasureRepository = generatedMeasureRepository;
    }

    public Windmill saveWindmill(Windmill windmill) {
        return windmillRepository.save(windmill);
    }

    public List<Windmill> getAllWindmills() {
        return windmillRepository.findAll();
    }
    public Optional<Windmill> getWindmill(UUID id) {
        return windmillRepository.findById(id);
    }

    public void activateWindmill(UUID id) {
        windmillRepository.findById(id).ifPresentOrElse(EnergyProducingDevice::activate,  () -> {
            throw new DeviceNotFoundException("Windmill with id " + id + " not found");});
    }

    public void deactivateWindmill(UUID id) {
        windmillRepository.findById(id).ifPresentOrElse(EnergyProducingDevice::deactivate, () -> {
            throw new DeviceNotFoundException("Windmill with id " + id + " not found");
        });
    }
    public Windmill deleteWindmill(UUID id) {
        var maybeWindmill = windmillRepository.findById(id);
        if (maybeWindmill.isEmpty()) {
            throw new DeviceNotFoundException("Windmill with id " + id + " not found");
        }
        windmillRepository.deleteById(id);
        return maybeWindmill.get();
    }

    @Scheduled(fixedRate = 300_000, initialDelay = 10000)
    public void simulateEnergyGeneration() {
        double windSpeed = simulator.getWindSpeed();
        windmillRepository.findByWorkingTrue().forEach(windmill -> generatedMeasureRepository
                .save(new GeneratedEnergyMeasure(LocalDateTime.now(),
                windmill.generateEnergy(windSpeed), windmill)));
    }
}
