package com.example.server.Simulation.service;


import com.example.server.Simulation.api.IShowDevices;
import com.example.server.Simulation.data.WindmillRepository;
import com.example.server.Simulation.entities.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShowDevicesService implements IShowDevices {

    private final SolarPanelService solarPanelService;

    private final WindmillService windmillService;

    private final ConsumingDeviceService consumingDeviceService;

    public ShowDevicesService(SolarPanelService solarPanelService, WindmillService windmillService, ConsumingDeviceService consumingDeviceService) {
        this.solarPanelService = solarPanelService;
        this.windmillService = windmillService;
        this.consumingDeviceService = consumingDeviceService;
    }

    @Override
    public List<Device> getDevices() {
        return List.of();
    }

    @Override
    public List<EnergyProducingDevice> getEnergyProducingDevices() {
        List<EnergyProducingDevice> energyProducingDevices = new ArrayList<>();
        energyProducingDevices.addAll(windmillService.getAllWindmills());
        energyProducingDevices.addAll(solarPanelService.getAllSolarPanels());
        return energyProducingDevices;
    }

    @Override
    public List<Windmill> getWindmills() {
        return windmillService.getAllWindmills();
    }

    @Override
    public List<SolarPanel> getSolarPanels() {
        return solarPanelService.getAllSolarPanels();
    }

    @Override
    public List<ConsumingDevice> getConsumingDevices() {
        return consumingDeviceService.getAllConsumingDevices();
    }

    @Override
    public Optional<SolarPanel> getSolarPanel(UUID id) {
        return solarPanelService.getSolarPanel(id);
    }

    @Override
    public Optional<ConsumingDevice> getConsumingDevice(UUID id) {
        return consumingDeviceService.getConsumingDevice(id);
    }

    @Override
    public Optional<Windmill> getWindmill(UUID id) {
        return windmillService.getWindmill(id);
    }

}
