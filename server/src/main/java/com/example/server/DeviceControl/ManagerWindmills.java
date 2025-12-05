package com.example.server.DeviceControl;

import com.example.server.Simulation.api.IControlDevices;
import com.example.server.Simulation.api.IShowDevices;
import com.example.server.Simulation.entities.Windmill;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerWindmills implements IDeviceAuth {

    private final IControlDevices controlDevices;
    private final IShowDevices showDevices;

    @Override
    public List<Device> getDevices() {
        return showDevices.getWindmills().stream()
                .map(w -> Device.builder()
                        .id(w.getId())
                        .name("Wiatrak " + w.getId().toString().substring(0, 4))
                        .isOn(w.isWorking())
                        .type("WIND")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean turnDeviceOn(UUID id) {
        if (showDevices.getWindmill(id).isPresent()) {
            controlDevices.activateWindmill(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean turnDeviceOff(UUID id) {
        if (showDevices.getWindmill(id).isPresent()) {
            controlDevices.deactivateWindmill(id);
            return true;
        }
        return false;
    }

    @Override
    public Device addDevice(String name) {

        Windmill newWindmill = new Windmill(false, 1000, 5);

        Windmill saved = controlDevices.addWindmill(newWindmill);

        return Device.builder()
                .id(saved.getId())
                .name(name)
                .type("WIND")
                .isOn(false)
                .build();
    }

    @Override
    public boolean removeDevice(UUID id) {
        if (showDevices.getWindmill(id).isPresent()) {
            controlDevices.removeWindmill(id);
            return true;
        }
        return false;
    }
}