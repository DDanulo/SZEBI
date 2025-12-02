package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Device;
import com.example.server.DeviceControl.IDeviceAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class DeviceController {


    private final List<IDeviceAuth> deviceManagers;

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();

        // Pobieramy urządzenia z każdego managera po kolei
        for (IDeviceAuth manager : deviceManagers) {
            allDevices.addAll(manager.getDevices());
        }

        return ResponseEntity.ok(allDevices);
    }

    @PostMapping("/{id}/turn-on")
    public ResponseEntity<Void> turnOn(@PathVariable UUID id) {

        for (IDeviceAuth manager : deviceManagers) {
            if (manager.turnDeviceOn(id)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build(); // Żaden menedżer nie znalazł takiego ID
    }


    @PostMapping("/{id}/turn-off")
    public ResponseEntity<Void> turnOff(@PathVariable UUID id) {
        for (IDeviceAuth manager : deviceManagers) {
            if (manager.turnDeviceOff(id)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}