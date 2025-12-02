package com.example.server.Simulation.data;

import com.example.server.Simulation.entities.ConsumingDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumingDeviceRepository extends JpaRepository<ConsumingDevice, UUID> {
}
