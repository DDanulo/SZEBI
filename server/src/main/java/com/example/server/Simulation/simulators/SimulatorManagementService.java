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

    public void changeSeason(Season season) {
        if(season == null) {
            throw new IllegalArgumentException("Season cannot be null");
        }
        simulator.changeSeason(season);
        getSimulationParameters();
    }

    public void changeTimeOfDay(TimeOfDay timeOfDay) {
        if(timeOfDay == null) {
            throw new IllegalArgumentException("TimeOfDay cannot be null");
        }
        simulator.changeTimeOfDay(timeOfDay);
        getSimulationParameters();
    }

    public Map<String, Object> getSimulationParameters() {
        return simulator.getSimulationParameters();
    }

    @Scheduled(fixedRate = 300_000)
    public void simulate() {
        simulator.simulate();
    }
}
