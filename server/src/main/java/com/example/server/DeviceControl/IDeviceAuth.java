package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import java.util.List;
import java.util.UUID;

public interface IDeviceAuth {
    boolean turnDeviceOn(UUID id);
    boolean turnDeviceOff(UUID id);

    // MAciek zrub coś
    Device addDevice(String name);

    boolean removeDevice(UUID id);
    List<Device> getDevices();
}