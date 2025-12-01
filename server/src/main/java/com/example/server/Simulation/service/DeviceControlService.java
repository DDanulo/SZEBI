package com.example.server.Simulation.service;

import com.example.server.Simulation.api.IControlDevices;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.entities.Windmill;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DeviceControlService implements IControlDevices {

    private final SolarPanelService solarPanelService;
    private final ConsumingDeviceService consumingDeviceService;
    private final WindmillService windmillService;


    public DeviceControlService(SolarPanelService solarPanelService,
                                ConsumingDeviceService consumingDeviceService,
                                WindmillService windmillService) {
        this.solarPanelService = solarPanelService;
        this.consumingDeviceService = consumingDeviceService;
        this.windmillService = windmillService;
    }

    @Override
    public SolarPanel addSolarPanel(SolarPanel solarPanel) {
        return solarPanelService.addSolarPanel(solarPanel);
    }

    @Override
    public SolarPanel removeSolarPanel(UUID id) {
        return solarPanelService.deleteSolarPanel(id);
    }

    @Override
    public void activateSolarPanel(UUID id) {
        solarPanelService.activateSolarPanel(id);
    }

    @Override
    public void deactivateSolarPanel(UUID id) {
        solarPanelService.deactivateSolarPanel(id);
    }

    @Override
    public Windmill addWindmill(Windmill windmill) {
        return windmillService.saveWindmill(windmill);
    }

    @Override
    public Windmill removeWindmill(UUID id) {
        return windmillService.deleteWindmill(id);
    }

    @Override
    public void activateWindmill(UUID id) {
        windmillService.activateWindmill(id);
    }

    @Override
    public void deactivateWindmill(UUID id) {
        windmillService.deactivateWindmill(id);
    }

    @Override
    public ConsumingDevice addConsumingDevice(ConsumingDevice consumingDevice) {
        return consumingDeviceService.addConsumingDevice(consumingDevice);
}

    @Override
    public ConsumingDevice removeConsumingDevice(UUID id) {
        return consumingDeviceService.deleteConsumingDevice(id);
    }

    @Override
    public void activateConsumingDevice(UUID id) {
        consumingDeviceService.activateConsumingDevice(id);
    }

    @Override
    public void deactivateConsumingDevice(UUID id) {
        consumingDeviceService.deactivateConsumingDevice(id);
    }
}
