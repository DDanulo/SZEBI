package com.example.server.Simulation.simulators;


import java.util.Map;


public interface Simulator {

    void simulate();
    Map<String, Object> getSimulationParameters();
}
