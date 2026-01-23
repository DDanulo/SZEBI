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
    private BigDecimal totalConsumed;

    @Column(name = "consuming_rate")
    private Double deviceConsumptionRate;

    public ConsumingDevice(String description, boolean active, Double deviceConsumptionRate) {
        super(description,active);
        this.totalConsumed = BigDecimal.valueOf(0.0f);
        this.deviceConsumptionRate = deviceConsumptionRate;
    }

    public double consumeEnergy(double coeff){
        double saveDeviceConsumptionRate = deviceConsumptionRate == null? 0: deviceConsumptionRate;
        double calculated = 2 * (45.0 - coeff) + saveDeviceConsumptionRate;
        this.totalConsumed = totalConsumed.add(BigDecimal.valueOf(calculated));
        return calculated;
    }



}
