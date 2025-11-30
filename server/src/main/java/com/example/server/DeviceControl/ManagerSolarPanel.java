package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManagerSolarPanel implements IDeviceAuth {

    // Symulacja bazy danych w pamięci
    private List<Device> solarPanels = new ArrayList<>();

    public ManagerSolarPanel() {
        // Używamy SZTYWNYCH ID, żeby przetrwały restart serwera
        solarPanels.add(new Device(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Panel Dach Północ", true, "SOLAR"));
        solarPanels.add(new Device(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Panel Garaż", false, "SOLAR"));
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

        Device newDevice = new Device(id, "Nowy Panel " + id.toString().substring(0, 4), false, "SOLAR");
        solarPanels.add(newDevice);
        return newDevice;
    }

    @Override
    public Device deleteDevice(UUID id) {
        Device toDelete = solarPanels.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
        if (toDelete != null) {
            solarPanels.remove(toDelete);
        }
        return toDelete;
    }

    @Override
    public List<Device> getDevices() {
        return new ArrayList<>(solarPanels);
    }

    // Metoda pomocnicza
    private boolean changeState(UUID id, boolean state) {
        for (Device d : solarPanels) {
            if (d.getId().equals(id)) {
                d.setOn(state);
                return true;
            }
        }
        return false;
    }
}