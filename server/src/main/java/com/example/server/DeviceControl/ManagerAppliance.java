package com.example.server.DeviceControl;

import com.example.server.Simulation.api.IControlDevices;
import com.example.server.Simulation.api.IShowDevices;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.DeviceControl.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerAppliance implements IDeviceAuth {

    private final IControlDevices controlDevices;
    private final IShowDevices showDevices;

    @Override
    public List<Device> getDevices() {
        return showDevices.getConsumingDevices().stream()
                .map(c -> Device.builder()
                        .id(c.getId())
                        .name(c.getDescription())
                        .type("APPLIANCE")
                        .isOn(c.isWorking())
<<<<<<< Updated upstream
=======
                        .area(c.getArea())
                        .totalConsumed(c.getTotalConsumed())
                        .totalGenerated(c.getTotalGenerated())
>>>>>>> Stashed changes
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean turnDeviceOn(UUID id) {
        if (showDevices.getConsumingDevice(id).isPresent()) {
            controlDevices.activateConsumingDevice(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean turnDeviceOff(UUID id) {
        if (showDevices.getConsumingDevice(id).isPresent()) {
            controlDevices.deactivateConsumingDevice(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(String type) {
        return "APPLIANCE".equalsIgnoreCase(type);
    }

    @Override
    public Device addDevice(String name, Double area, Integer maxPower, Integer minWind) {
         ConsumingDevice device = new ConsumingDevice(name, true);


        var saved = controlDevices.addConsumingDevice(device);

        return Device.builder()
                .id(saved.getId())
                .name(name)
                .type("APPLIANCE")
                .isOn(false)
                .build();
    }




    @Override
    public boolean removeDevice(UUID id) {
        if (showDevices.getConsumingDevice(id).isPresent()) {
            controlDevices.removeConsumingDevice(id);
            return true;
        }
        return false;
    }
}