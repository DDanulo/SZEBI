<<<<<<<< HEAD:server/src/main/java/com/example/server/DeviceControl/manager/ManagerWindmills.java
package com.example.server.DeviceControl.manager;

import com.example.server.DeviceControl.entities.Device;
import com.example.server.DeviceControl.api.IDeviceAuth;
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
                        .name(w.getDescription())
                        .isOn(w.isWorking())
                        .type("WIND")
                    /*    .area(w.getArea())
                        .totalConsumed(w.getTotalConsumed())*/
                        .totalGenerated(w.getTotalGenerated())
                        .ownerId(w.getOwnerId())
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
    public Device addDevice(String name, Double area, Integer maxPower, Integer minWind, UUID ownerId) {


        int safeMaxPower = (maxPower != null) ? maxPower : 0;
        int safeMinWind = (minWind != null) ? minWind : 0;
        double safeArea = (area != null) ? area : 0.0;

        Windmill windmill = new Windmill(name,false, safeMaxPower, safeMinWind);

        windmill.setOwnerId(ownerId);


        var saved = controlDevices.addWindmill(windmill);

        return Device.builder()
                .id(saved.getId())
                .name(name)
                .type("WIND")
                .isOn(false)
                .area(safeArea)
                .ownerId(ownerId)
                .build();
    }

    @Override
    public boolean supports(String type) {
        return "WIND".equalsIgnoreCase(type);
    }




    @Override
    public boolean removeDevice(UUID id) {
        if (showDevices.getWindmill(id).isPresent()) {
            controlDevices.removeWindmill(id);
            return true;
        }
        return false;
    }
========
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
                        .name(w.getDescription())
                        .isOn(w.isWorking())
                        .type("WIND")
<<<<<<< Updated upstream
=======
                        .area(w.getArea())
                        .totalConsumed(w.getTotalConsumed())
                        .totalGenerated(w.getTotalGenerated())
>>>>>>> Stashed changes
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
    public Device addDevice(String name, Double area, Integer maxPower, Integer minWind) {

        Windmill windmill = new Windmill(name,false, maxPower, minWind);



        var saved = controlDevices.addWindmill(windmill);

        return Device.builder()
                .id(saved.getId())
                .name(name)
                .type("WIND")
                .isOn(false)
                .area(area)
                .build();
    }

    @Override
    public boolean supports(String type) {
        return "WIND".equalsIgnoreCase(type);
    }




    @Override
    public boolean removeDevice(UUID id) {
        if (showDevices.getWindmill(id).isPresent()) {
            controlDevices.removeWindmill(id);
            return true;
        }
        return false;
    }
>>>>>>>> origin/DeviceControl-vlad-Adam:server/src/main/java/com/example/server/DeviceControl/ManagerWindmills.java
}