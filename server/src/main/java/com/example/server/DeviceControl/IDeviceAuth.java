package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import java.util.List;
import java.util.UUID;

public interface IDeviceAuth {
    boolean turnDeviceOn(UUID id);
    boolean turnDeviceOff(UUID id);
    Device addDevice(UUID id); // Zgodnie z diagramem przyjmujemy UUID
    Device deleteDevice(UUID id);
    List<Device> getDevices();
}