package com.example.server.simulation;

import com.example.server.Simulation.simulators.BasicSimulator;
import com.example.server.Simulation.simulators.Season;
import com.example.server.Simulation.simulators.TimeOfDay;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = BasicSimulator.class)
public class SimulationTest {

    private BasicSimulator simulator;



    @Test
    public void shouldChangeDayTimeAfterCertainPeriod() {
        simulator = new BasicSimulator();
        TimeOfDay dayTime = simulator.getDayTime();
        for(int i= 0; i < 52; i++) {
            simulator.simulate();
        }
        assertEquals(simulator.getDayTime(), dayTime.next());
    }

    @Test
    public void shouldChangeSeasonAfterCertainPeriod() {
        simulator = new BasicSimulator();
        Season season = simulator.getSeason();
        for(int i= 0; i < 120; i++) {
            simulator.simulate();
        }
        assertNotEquals(season,  simulator.getSeason());
    }
}
