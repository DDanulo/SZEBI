package com.example.server.Simulation.api;

import com.example.server.Simulation.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IShowDevices {

    List<Device> getDevices();
    List<EnergyProducingDevice> getEnergyProducingDevices();
    List<Windmill> getWindmills();
    List<SolarPanel> getSolarPanels();
    List<ConsumingDevice> getConsumingDevices();
    Optional<SolarPanel> getSolarPanel(UUID id);
    Optional<ConsumingDevice> getConsumingDevice(UUID id);
    Optional<Windmill> getWindmill(UUID id);
}
