package com.example.server.Simulation.simulators;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimulatorManagementService {

    private final BasicSimulator simulator;

    public SimulatorManagementService(BasicSimulator simulator) {
        this.simulator = simulator;
    }

    public Map<String, Object> changeSeason(Season season) {
        simulator.changeSeason(season);
        return getSimulationParameters();
    }

    public Map<String, Object> changeTimeOfDay(TimeOfDay timeOfDay) {
        simulator.changeTimeOfDay(timeOfDay);
        return getSimulationParameters();
    }

    public Map<String, Object> getSimulationParameters() {
        return simulator.getSimulationParameters();
    }

    @Scheduled(fixedRate = 300_000)
    public void simulate() {
        simulator.simulate();
    }
}
