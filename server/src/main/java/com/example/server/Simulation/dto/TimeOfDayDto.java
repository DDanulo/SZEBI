package com.example.server.Simulation.dto;

import com.example.server.Simulation.simulators.TimeOfDay;
import lombok.Builder;


public record TimeOfDayDto(
        TimeOfDay timeOfDay
) {
}
