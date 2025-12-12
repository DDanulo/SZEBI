package com.example.server.Simulation.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@DiscriminatorValue("generated")
public class GeneratedEnergyMeasure extends EnergyMeasure {


    public GeneratedEnergyMeasure(LocalDateTime timestamp, double value, Device device) {
        super(timestamp, value, device);
    }
}
