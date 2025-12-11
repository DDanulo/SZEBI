package com.example.server.Simulation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("solar_panel")
public class SolarPanel extends EnergyProducingDevice{

    @Column
    private double area;

    @Override
    public double generateEnergy(double insolation) {
        if(this.totalGenerated == null){
            this.totalGenerated = BigDecimal.ZERO;
        }
        double generatedNow = area * insolation * 0.3;
        this.totalGenerated = totalGenerated.add(BigDecimal.valueOf(generatedNow));
        return generatedNow;
    }

    public SolarPanel(boolean working, double area) {
        super(working);
        this.area = area;
    }


}
