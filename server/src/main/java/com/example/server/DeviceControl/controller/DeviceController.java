package com.example.server.DeviceControl.controller;

import com.example.server.DeviceControl.entities.Device;
import com.example.server.DeviceControl.api.IDeviceAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class DeviceController {

    private final List<IDeviceAuth> deviceManagers;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'RESIDENT')")
    public ResponseEntity<List<Device>> getAllDevices() {
        // 1. Pobierz wszystko
        List<Device> allDevices = new ArrayList<>();
        for (IDeviceAuth manager : deviceManagers) {
            allDevices.addAll(manager.getDevices());
        }

        // 2. Pobierz info o użytkowniku z Tokena
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Zabezpieczenie: jeśli nikt nie jest zalogowany (powinno być niemożliwe przez @PreAuthorize, ale warto mieć)
        if (auth == null) return ResponseEntity.status(401).build();

        // Sprawdzamy role
        boolean isAdminOrEngineer = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_ENGINEER"));

        if (isAdminOrEngineer) {
            // Admin widzi wszystko
            return ResponseEntity.ok(allDevices);
        } else {
            // Resident widzi TYLKO SWOJE (po ownerId)
            try {
                UUID currentUserId = UUID.fromString(auth.getName());
                List<Device> myDevices = allDevices.stream()
                        .filter(d -> d.getOwnerId() != null && d.getOwnerId().equals(currentUserId))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(myDevices);
            } catch (Exception e) {
                // Jeśli nazwa w tokenie nie jest UUID, to coś jest nie tak z konfiguracją auth
                return ResponseEntity.status(403).build();
            }
        }
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
@GetMapping("/user/{ownerId}")
@PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
public ResponseEntity<List<Device>> getDevicesByOwner(@PathVariable UUID ownerId) {
    List<Device> allDevices = new ArrayList<>();


    for (IDeviceAuth manager : deviceManagers) {
        allDevices.addAll(manager.getDevices());
    }


    List<Device> userDevices = allDevices.stream()
            .filter(d -> d.getOwnerId() != null && d.getOwnerId().equals(ownerId))
            .collect(Collectors.toList());

    return ResponseEntity.ok(userDevices);
}

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID ownerId = UUID.fromString(auth.getName());

        for (IDeviceAuth manager : deviceManagers) {
             if (manager.supports(type)) {
                 return ResponseEntity.ok(
                        manager.addDevice(name, area, safeMaxPower, safeMinWind, ownerId)
                );
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<Void> removeDevice(@PathVariable UUID id) {
        for (IDeviceAuth manager : deviceManagers) {
            if (manager.removeDevice(id)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}