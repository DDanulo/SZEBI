package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import java.util.List;
import java.util.UUID;

public interface IDeviceAuth {
    boolean turnDeviceOn(UUID id);
    boolean turnDeviceOff(UUID id);

    // MAciek zrub coś
   boolean supports(String type);

    Device addDevice(String name, Double area, Integer maxPower, Integer minWind);
    boolean removeDevice(UUID id);
    List<Device> getDevices();
}