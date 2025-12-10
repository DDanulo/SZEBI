package com.example.server.DataAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPoint {
    private LocalDateTime timestamp;
    private double value;
    private DataType type;
    private String deviceId;
}