package com.example.server.Simulation.endpoints;

import com.example.server.Simulation.dto.SeasonDto;
import com.example.server.Simulation.dto.SimulationParametersDto;
import com.example.server.Simulation.dto.TimeOfDayDto;
import com.example.server.Simulation.simulators.Season;
import com.example.server.Simulation.simulators.TimeOfDay;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
class DtoToBusinessLayerMapper {



    SimulationParametersDto mapSimulationParametersToDto(Map<String, Object> map) {
        return SimulationParametersDto.builder()
                .season((String) map.get("season"))
                .timeOfDay((String) map.get("dayTime"))
                .temperature((double) map.get("temperature"))
                .insolation((float) map.get("insolation"))
                .windSpeed((double) map.get("windSpeed"))
                .build();
    }
}
