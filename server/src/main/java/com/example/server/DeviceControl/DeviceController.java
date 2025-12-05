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
    public ResponseEntity<Device> addDevice(@RequestParam String name, @RequestParam String type) {

        for (IDeviceAuth manager : deviceManagers) {



            if ("SOLAR".equalsIgnoreCase(type) && manager.getClass().getSimpleName().contains("Solar")) {
                return ResponseEntity.ok(manager.addDevice(name));
            }

            if ("WIND".equalsIgnoreCase(type) && manager.getClass().getSimpleName().contains("Wind")) {
                return ResponseEntity.ok(manager.addDevice(name));
            }

            if ("APPLIANCE".equalsIgnoreCase(type) && manager.getClass().getSimpleName().contains("Appliance")) {
                return ResponseEntity.ok(manager.addDevice(name));
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