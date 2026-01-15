package com.example.server.DeviceControl.manager;

import com.example.server.DeviceControl.entities.Device;
import com.example.server.DeviceControl.api.IDeviceAuth;
import com.example.server.Simulation.api.IControlDevices;
import com.example.server.Simulation.api.IShowDevices;
import com.example.server.Simulation.entities.SolarPanel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerSolarPanel implements IDeviceAuth {

    private final IControlDevices controlDevices;
    private final IShowDevices showDevices;

    @Override
    public List<Device> getDevices() {
        return showDevices.getSolarPanels().stream()
                .map(p -> Device.builder()
                        .id(p.getId())
                        .name(p.getDescription())
                        .type("SOLAR")
                        .isOn(p.isWorking())
                        .area(p.getArea())
                       /* .totalConsumed(p.getTotalConsumed())*/
                        .totalGenerated(p.getTotalGenerated())
                        .ownerId(p.getOwnerId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean turnDeviceOn(UUID id) {
        if (showDevices.getSolarPanel(id).isPresent()) {
            controlDevices.activateSolarPanel(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean turnDeviceOff(UUID id) {
        if (showDevices.getSolarPanel(id).isPresent()) {
            controlDevices.deactivateSolarPanel(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(String type) {
        return "SOLAR".equalsIgnoreCase(type);
    }

    @Override
    public Device addDevice(String name, Double area, Integer maxPower, Integer minWind, UUID ownerId) {
        SolarPanel panel = new SolarPanel(name,true, area);
        panel.setOwnerId(ownerId);
        var saved = controlDevices.addSolarPanel(panel);

        return Device.builder()
                .id(saved.getId())
                .name(panel.getDescription())
                .type("SOLAR")
                .isOn(false)
                .area(area)
                .ownerId(ownerId)
                .build();
    }



    @Override
    public boolean removeDevice(UUID id) {
        if (showDevices.getSolarPanel(id).isPresent()) {
            controlDevices.removeSolarPanel(id);
            return true;
        }
        return false;
    }
}