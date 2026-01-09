package com.example.server.Simulation.dto;

import com.example.server.Simulation.simulators.Season;
import lombok.Builder;

@Builder
public record SeasonDto(
        Season season
) {
}
