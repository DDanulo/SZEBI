package com.example.server.Simulation.simulators;


import java.util.Map;


public interface Simulator {

    void simulate();

    Map<String, Object> getSimulationParameters();
}
    //do zastanowienia: czy change daytime i change season powinny byc w tez w interfejsie
    //toDo: ewentualne jescze 2 usecase-y
