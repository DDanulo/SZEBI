package com.example.server.DeviceControl.service;

import com.example.server.DeviceControl.entities.DeviceRequest;
import com.example.server.DeviceControl.api.IDeviceAuth;
import com.example.server.DeviceControl.repo.DeviceRequestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final DeviceRequestRepo requestRepo;
    private final List<IDeviceAuth> deviceManagers; // Do faktycznego tworzenia/usuwania

    // 1. Resident tworzy prośbę o dodanie
    public void createAddRequest(UUID userId, String name, String type, Double area, Integer maxP, Integer minW) {
        DeviceRequest req = DeviceRequest.builder()
                .userId(userId)
                .requestType(DeviceRequest.RequestType.ADD)
                .status(DeviceRequest.RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .deviceName(name)
                .deviceType(type)
                .area(area)
                .maxPower(maxP)
                .minWind(minW)
                .build();
        requestRepo.save(req);
    }

    // 2. Resident tworzy prośbę o usunięcie
    public void createRemoveRequest(UUID userId, UUID deviceId) {
        DeviceRequest req = DeviceRequest.builder()
                .userId(userId)
                .requestType(DeviceRequest.RequestType.REMOVE)
                .status(DeviceRequest.RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .targetDeviceId(deviceId)
                .build();
        requestRepo.save(req);
    }

    // 3. Admin zatwierdza wniosek
    public void approveRequest(UUID requestId) {
        DeviceRequest req = requestRepo.findById(requestId).orElseThrow();

        if (req.getRequestType() == DeviceRequest.RequestType.ADD) {
            // Szukamy odpowiedniego managera i tworzymy urządzenie
            for (IDeviceAuth manager : deviceManagers) {
                if (manager.supports(req.getDeviceType())) {
                    // UWAGA: Tu przekazujemy ID użytkownika z wniosku jako właściciela!
                    manager.addDevice(req.getDeviceName(), req.getArea(), req.getMaxPower(), req.getMinWind(), req.getUserId());
                    break;
                }
            }
        } else if (req.getRequestType() == DeviceRequest.RequestType.REMOVE) {
            // Usuwamy urządzenie
            for (IDeviceAuth manager : deviceManagers) {
                if (manager.removeDevice(req.getTargetDeviceId())) break;
            }
        }

        req.setStatus(DeviceRequest.RequestStatus.APPROVED);
        requestRepo.save(req);
    }

    // 4. Admin odrzuca wniosek
    public void rejectRequest(UUID requestId) {
        DeviceRequest req = requestRepo.findById(requestId).orElseThrow();
        req.setStatus(DeviceRequest.RequestStatus.REJECTED);
        requestRepo.save(req);
    }

    // Pobieranie listy oczekujących (dla Admina)
    public List<DeviceRequest> getPendingRequests() {
        return requestRepo.findByStatus(DeviceRequest.RequestStatus.PENDING);
    }
}