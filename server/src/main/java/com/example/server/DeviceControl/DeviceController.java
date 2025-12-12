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
        for (IDeviceAuth manager : deviceManagers) {
            allDevices.addAll(manager.getDevices());
        }
        return ResponseEntity.ok(allDevices);
    }

    @PostMapping("/{id}/turn-on")
    public ResponseEntity<Void> turnOn(@PathVariable UUID id) {
        for (IDeviceAuth manager : deviceManagers) {
            if (manager.turnDeviceOn(id)) return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/turn-off")
    public ResponseEntity<Void> turnOff(@PathVariable UUID id) {
        for (IDeviceAuth manager : deviceManagers) {
            if (manager.turnDeviceOff(id)) return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


// Maciak prosze bardzo

    @PostMapping
    public ResponseEntity<Device> addDevice(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam(required = true) Double area,
            @RequestParam(required = false) Integer maxPower,
            @RequestParam(required = false) Integer minWind
    ) {
        if (area == null || area <= 0) {
            return ResponseEntity.badRequest().build();
        }

        int safeMaxPower = (maxPower != null) ? maxPower : 0;
        int safeMinWind = (minWind != null) ? minWind : 0;

        for (IDeviceAuth manager : deviceManagers) {
             if (manager.supports(type)) {
                 return ResponseEntity.ok(
                        manager.addDevice(name, area, safeMaxPower, safeMinWind)
                );
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDevice(@PathVariable UUID id) {
        for (IDeviceAuth manager : deviceManagers) {
            if (manager.removeDevice(id)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}