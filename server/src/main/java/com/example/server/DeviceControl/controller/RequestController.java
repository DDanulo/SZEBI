package com.example.server.DeviceControl.controller;

import com.example.server.DeviceControl.entities.DeviceRequest;
import com.example.server.DeviceControl.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RequestController {

    private final RequestService requestService;

    // RESIDENT: Zgłoś chęć dodania urządzenia
    @PostMapping("/add")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<Void> requestAddDevice(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam Double area,
            @RequestParam(required = false) Integer maxPower,
            @RequestParam(required = false) Integer minWind
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        requestService.createAddRequest(userId, name, type, area, maxPower, minWind);
        return ResponseEntity.ok().build();
    }

    // RESIDENT: Zgłoś chęć usunięcia
    @PostMapping("/remove/{deviceId}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<Void> requestRemoveDevice(@PathVariable UUID deviceId) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        requestService.createRemoveRequest(userId, deviceId);
        return ResponseEntity.ok().build();
    }

    // ADMIN/ENGINEER: Pobierz listę wniosków
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<List<DeviceRequest>> getPendingRequests() {
        return ResponseEntity.ok(requestService.getPendingRequests());
    }

    // ADMIN/ENGINEER: Zatwierdź
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<Void> approve(@PathVariable UUID id) {
        try {
            requestService.approveRequest(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ADMIN/ENGINEER: Odrzuć
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        requestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }
}