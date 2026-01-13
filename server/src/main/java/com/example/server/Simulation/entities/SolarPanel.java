package com.example.server.Simulation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("solar_panel")
public class SolarPanel extends EnergyProducingDevice{

    @Getter
    @Column
    private double area;

    @Override
    public double generateEnergy(double insolation) {
        double generatedNow = area * insolation * 0.3;
        this.totalGenerated = totalGenerated.add(BigDecimal.valueOf(generatedNow));
        return generatedNow;
    }

    public SolarPanel(String description, boolean working, double area) {
        super(description, working);
        this.area = area;
    }


}
