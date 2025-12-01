package com.example.server.Simulation.service;


import com.example.server.Simulation.data.WindmillRepository;
import com.example.server.Simulation.simulators.BasicSimulator;
import org.springframework.stereotype.Service;

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
                } catch (InterruptedException e) {}
            }
        });
        this.windmillRepository = windmillRepository;
    }

    private void simulateEnergyGeneration() {
        double windSpeed = simulator.getWindSpeed();
        windmillRepository.findAll().forEach(windmill -> {windmill.generateEnergy(windSpeed);});
    }
}
