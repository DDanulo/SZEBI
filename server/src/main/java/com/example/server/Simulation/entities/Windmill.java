package com.example.server.Simulation.entities;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@DiscriminatorValue("windmill")
public class Windmill extends EnergyProducingDevice{

    @Column
    private int maxPowerPerHour;

    @Column
    private int minWindSpeedForMaxPower;

    @Override
    public double generateEnergy(double windSpeed) {
        double generatedNow =  maxPowerPerHour * Math.min(windSpeed/minWindSpeedForMaxPower, 1) /12;
        totalGenerated.add(BigDecimal.valueOf(generatedNow));
        return generatedNow;
    }
}
