package com.example.server.Simulation.service;


import com.example.server.Simulation.data.WindmillRepository;
import com.example.server.Simulation.entities.EnergyProducingDevice;
import com.example.server.Simulation.entities.Windmill;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WindmillService {

    private final BasicSimulator simulator;

    private final Thread simulationThread;

    private final WindmillRepository windmillRepository;

    public WindmillService(BasicSimulator simulator, WindmillRepository windmillRepository) {
        this.simulator = simulator;
        this.simulationThread = new Thread(() -> {
            while (true) {
                simulateEnergyGeneration();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.windmillRepository = windmillRepository;
    }

    @PostConstruct
    public void init() {
        simulationThread.start();
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
    private void simulateEnergyGeneration() {
        double windSpeed = simulator.getWindSpeed();
        windmillRepository.findAll().forEach(windmill -> {windmill.generateEnergy(windSpeed);});
    }
}
