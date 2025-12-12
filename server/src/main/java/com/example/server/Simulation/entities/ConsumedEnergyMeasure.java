package com.example.server.Simulation.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("consumed")
@NoArgsConstructor
public class ConsumedEnergyMeasure extends EnergyMeasure {

    public ConsumedEnergyMeasure(LocalDateTime timestamp, double value, Device device) {
        super(timestamp, value, device);
    }
}
