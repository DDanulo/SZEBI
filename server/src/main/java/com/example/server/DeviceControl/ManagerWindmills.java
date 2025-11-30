package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerWindmills implements IDeviceAuth {

    private List<Device> windmills = new ArrayList<>();

    public ManagerWindmills() {
        windmills.add(new Device(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Turbina Główna", true, "WIND"));
        windmills.add(new Device(UUID.fromString("44444444-4444-4444-4444-444444444444"), "Wiatrak Ogrodowy", true, "WIND"));
    }

    @Override
    public boolean turnDeviceOn(UUID id) {
        return changeState(id, true);
    }

    @Override
    public boolean turnDeviceOff(UUID id) {
        return changeState(id, false);
    }

    @Override
    public Device addDevice(UUID id) {
        Device newDevice = new Device(id, "Wiatrak " + id.toString().substring(0, 4), false, "WIND");
        windmills.add(newDevice);
        return newDevice;
    }

    @Override
    public Device deleteDevice(UUID id) {
        Device toDelete = windmills.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
        if (toDelete != null) windmills.remove(toDelete);
        return toDelete;
    }

    @Override
    public List<Device> getDevices() {
        return new ArrayList<>(windmills);
    }

    private boolean changeState(UUID id, boolean state) {
        for (Device d : windmills) {
            if (d.getId().equals(id)) {
                d.setOn(state);
                return true;
            }
        }
        return false;
    }
}