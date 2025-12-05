package com.example.server.DeviceControl;

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
                        .name("Panel Solarny " + p.getId().toString().substring(0, 4))
                        .isOn(p.isWorking())
                        .type("SOLAR")
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
    public Device addDevice(String name) {
        // Konstruktor SolarPanel wymaga: (boolean working, double area)
        // Ustawiamy domyślne area = 20.0, bo frontend tego nie podaje
        SolarPanel newPanel = new SolarPanel(false, 20.0);

        // ID generuje się w bazie (GenerationType.IDENTITY) lub musimy nadać
        // Skoro w Device.java jest IDENTITY, to baza nada ID po zapisie.
        // Ale ControlDevices.addSolarPanel pewnie robi save().

        SolarPanel saved = controlDevices.addSolarPanel(newPanel);

        return Device.builder()
                .id(saved.getId())
                .name(name) // Używamy nazwy podanej przez użytkownika w widoku
                .type("SOLAR")
                .isOn(false)
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