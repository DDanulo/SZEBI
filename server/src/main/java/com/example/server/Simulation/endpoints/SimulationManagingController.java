package com.example.server.Simulation.endpoints;

import com.example.server.Simulation.dto.SeasonDto;
import com.example.server.Simulation.dto.SimulationParametersDto;
import com.example.server.Simulation.dto.TimeOfDayDto;
import com.example.server.Simulation.simulators.Season;
import com.example.server.Simulation.simulators.SimulatorManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
class SimulationManagingController {

    private final SimulatorManagementService simulatorManagementService;
    private final DtoToBusinessLayerMapper dtoToBusinessLayerMapper;

    public SimulationManagingController(SimulatorManagementService simulatorManagementService, DtoToBusinessLayerMapper dtoToBusinessLayerMapper) {
        this.simulatorManagementService = simulatorManagementService;
        this.dtoToBusinessLayerMapper = dtoToBusinessLayerMapper;
    }

    @PutMapping("/season")
    @ResponseStatus(HttpStatus.OK)
    public SimulationParametersDto changeSeason(@RequestBody SeasonDto seasonDto) {
        simulatorManagementService.changeSeason(seasonDto.season());
        return dtoToBusinessLayerMapper
                .mapSimulationParametersToDto(simulatorManagementService.getSimulationParameters());
    }

    @PutMapping("/daytime")
    @ResponseStatus(HttpStatus.OK)
    public SimulationParametersDto changeDaytime(@RequestBody TimeOfDayDto timeOfDayDto) {
        simulatorManagementService.changeTimeOfDay(timeOfDayDto.timeOfDay());
        return dtoToBusinessLayerMapper
                .mapSimulationParametersToDto(simulatorManagementService.getSimulationParameters());
    }

    @GetMapping("/parameters")
    @ResponseStatus(HttpStatus.OK)
    public SimulationParametersDto getSimulationParameters() {
        return dtoToBusinessLayerMapper.mapSimulationParametersToDto(simulatorManagementService.getSimulationParameters());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
