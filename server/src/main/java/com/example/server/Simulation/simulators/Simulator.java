package com.example.server.Simulation.simulators;

import lombok.Getter;

import java.util.List;
import java.util.Map;


public interface Simulator {

    void simulate();
    Map<String, Object> getSimulationParameters();
}
