package com.example.server.DeviceControl.repo;

import com.example.server.DeviceControl.entities.DeviceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRequestRepo extends JpaRepository<DeviceRequest, UUID> {
    List<DeviceRequest> findByStatus(DeviceRequest.RequestStatus status);
    List<DeviceRequest> findByUserId(UUID userId);
}
