package com.example.server.Simulation.simulators;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimulatorManagementService {

    private final BasicSimulator simulator;

    public SimulatorManagementService(BasicSimulator simulator) {
        this.simulator = simulator;
    }

    public Map<String, Object> changeSeason(Season season) {
        simulator.setSeason(season);
        return simulator.getSimulationParameters();
    }

    public Map<String, Object> changeTimeOfDay(TimeOfDay timeOfDay) {
        simulator.changeTimeOfDay(timeOfDay);
        return simulator.getSimulationParameters();
    }
}
