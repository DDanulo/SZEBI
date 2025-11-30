package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerAppliance implements IDeviceAuth {

    private List<Device> appliances = new ArrayList<>();

    public ManagerAppliance() {
        appliances.add(new Device(UUID.fromString("55555555-5555-5555-5555-555555555555"), "Lodówka Samsung", true, "APPLIANCE"));
        appliances.add(new Device(UUID.fromString("66666666-6666-6666-6666-666666666666"), "Telewizor Salon", false, "APPLIANCE"));
        appliances.add(new Device(UUID.fromString("77777777-7777-7777-7777-777777777777"), "Ekspres do kawy", false, "APPLIANCE"));
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
        Device newDevice = new Device(id, "Sprzęt " + id.toString().substring(0, 4), false, "APPLIANCE");
        appliances.add(newDevice);
        return newDevice;
    }

    @Override
    public Device deleteDevice(UUID id) {
        Device toDelete = appliances.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
        if (toDelete != null) appliances.remove(toDelete);
        return toDelete;
    }

    @Override
    public List<Device> getDevices() {
        return new ArrayList<>(appliances);
    }

    private boolean changeState(UUID id, boolean state) {
        for (Device d : appliances) {
            if (d.getId().equals(id)) {
                d.setOn(state);
                return true;
            }
        }
        return false;
    }
}