package com.example.server.DataPrediction.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecord {
    public LocalDateTime timestamp;
    public double value;

    @Override
    public String toString() {
        return "UsageRecord{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
