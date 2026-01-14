package com.example.server.Simulation.dto;

import com.example.server.Simulation.simulators.Season;
import com.example.server.Simulation.simulators.TimeOfDay;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record SimulationParametersDto(
        String season,
        @JsonProperty("time_of_day") String timeOfDay,
        double temperature,
        @JsonProperty("wind_speed") double windSpeed,
        float insolation
) {
}
