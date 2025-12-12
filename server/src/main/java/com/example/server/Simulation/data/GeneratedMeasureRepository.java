package com.example.server.Simulation.data;

import com.example.server.Simulation.entities.GeneratedEnergyMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeneratedMeasureRepository extends JpaRepository<GeneratedEnergyMeasure, UUID> {
}
