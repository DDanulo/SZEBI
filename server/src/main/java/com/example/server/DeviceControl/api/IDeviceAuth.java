package com.example.server.DeviceControl.api;

import com.example.server.DeviceControl.entities.Device;
import java.util.List;
import java.util.UUID;

public interface IDeviceAuth {
    boolean turnDeviceOn(UUID id);
    boolean turnDeviceOff(UUID id);

    // MAciek zrub coś
   boolean supports(String type);

    Device addDevice(String name, Double area, Integer maxPower, Integer minWind, UUID ownerId);
    boolean removeDevice(UUID id);
    List<Device> getDevices();
}