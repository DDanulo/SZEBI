package com.example.server.Simulation.data;

import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.entities.Windmill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WindmillRepository extends JpaRepository<Windmill, UUID> {

    public List<Windmill> findByWorkingTrue();
}
