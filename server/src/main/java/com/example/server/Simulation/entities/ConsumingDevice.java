package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class ConsumingDevice extends Device {


    @Column
    private String description;


    @Column
    private BigDecimal totalConsumed;


    public ConsumingDevice(String description, boolean active) {
        super(active);
        this.description = description;
    }

    public double consumeEnergy(double coeff){
        double calculated = 5 * (45.0 - coeff);
        totalConsumed.add(BigDecimal.valueOf(calculated));
        return calculated;
    }



}
