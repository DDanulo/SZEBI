package com.example.server.Simulation.entities;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@DiscriminatorValue("windmill")
@NoArgsConstructor
public class Windmill extends EnergyProducingDevice{

    @Column
    private int maxPowerPerHour;

    @Column
    private int minWindSpeedForMaxPower;


    @Override
    public double generateEnergy(double windSpeed) {
        if(this.totalGenerated == null){
            this.totalGenerated = BigDecimal.ZERO;
        }
        double generatedNow =  maxPowerPerHour * Math.min(windSpeed/minWindSpeedForMaxPower, 1) /12;
        this.totalGenerated = totalGenerated.add(BigDecimal.valueOf(generatedNow));
        return generatedNow;
    }

    public Windmill(String description, boolean working, int maxPowerPerHour, int minWindSpeedForMaxPower) {
        super(description,working);
        if (maxPowerPerHour <= 0) {
            this.maxPowerPerHour = 150;
        } else {
            this.maxPowerPerHour = maxPowerPerHour;
        }
        if (minWindSpeedForMaxPower <= 0) {
            this.minWindSpeedForMaxPower = 25;
        } else {
            this.minWindSpeedForMaxPower = minWindSpeedForMaxPower;
        }
    }
}
