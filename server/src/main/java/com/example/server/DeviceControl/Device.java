package com.example.server.DeviceControl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Device {
    private UUID id;
    private String name;
    private boolean isOn;
    private String type;
    private double area;

    private BigDecimal totalConsumed;
    private BigDecimal totalGenerated;

}