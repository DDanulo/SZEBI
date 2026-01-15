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
        //ostatni paramatre to czas zeby sie zgadzała jednostka (czas)
        //bo tak jest (w/m^2) * m^2 (czyli moc) * s (czas) to energia/praca
        double generatedNow = area * insolation * 0.3 *(1/12.0);
        this.totalGenerated = totalGenerated.add(BigDecimal.valueOf(generatedNow));
        return generatedNow;
    }

    public SolarPanel(String description, boolean working, double area) {
        super(description, working);
        if(area <= 0.001) {
            this.area = 5.0;
        } else {
            this.area = area;
        }
    }


}
