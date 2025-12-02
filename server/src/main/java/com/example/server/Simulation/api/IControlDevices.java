package com.example.server.Simulation.api;

import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.entities.Windmill;

import java.util.UUID;

public interface IControlDevices {

    SolarPanel addSolarPanel(SolarPanel solarPanel);
    SolarPanel removeSolarPanel(UUID id);
    void activateSolarPanel(UUID id);
    void deactivateSolarPanel(UUID id);
    Windmill addWindmill(Windmill windmill);
    Windmill removeWindmill(UUID id);
    void activateWindmill(UUID id);
    void deactivateWindmill(UUID id);
    ConsumingDevice  addConsumingDevice(ConsumingDevice consumingDevice);
    ConsumingDevice removeConsumingDevice(UUID id);
    void activateConsumingDevice(UUID id);
    void deactivateConsumingDevice(UUID id);

}
