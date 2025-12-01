package com.example.server.Simulation.endpoints;

import com.example.server.Simulation.simulators.Season;
import com.example.server.Simulation.simulators.SimulatorManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/simulation")
class SimulationManagingController {

    private final SimulatorManagementService simulatorManagementService;

    public SimulationManagingController(SimulatorManagementService simulatorManagementService) {
        this.simulatorManagementService = simulatorManagementService;
    }

    @PutMapping("/season")
    public ResponseEntity<?> changeSeason(@RequestBody Season season) {
       return null;
    }

//    @PutMapping("/daytime")
//    public
}
