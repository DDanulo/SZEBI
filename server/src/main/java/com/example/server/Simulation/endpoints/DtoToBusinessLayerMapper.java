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
                .temperature( Math.ceil( (double)map.get("temperature") *100) / 100)
                .insolation((float) Math.ceil((float)map.get("insolation") *100) /100)
                .windSpeed(Math.ceil((double)map.get("windSpeed") *100) /100)
                .build();
    }
}
