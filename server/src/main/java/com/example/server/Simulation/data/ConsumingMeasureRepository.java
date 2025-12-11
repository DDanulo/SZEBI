package com.example.server.Simulation.data;

import com.example.server.Simulation.entities.ConsumedEnergyMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumingMeasureRepository extends JpaRepository<ConsumedEnergyMeasure, UUID> {
}
