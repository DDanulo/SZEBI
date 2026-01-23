package com.example.server.simulation;


import com.example.server.Simulation.data.GeneratedMeasureRepository;
import com.example.server.Simulation.entities.GeneratedEnergyMeasure;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.entities.Windmill;
import com.example.server.Simulation.service.WindmillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//poprawic konstruktory....
@Testcontainers
@Transactional
@SpringBootTest
public class WindmillGenerationTest {

    @Autowired
    private WindmillService  windmillService;

    @Autowired
    private GeneratedMeasureRepository  generatedMeasureRepository;


    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("iouser")
            .withPassword("1234");

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }


    @Test
    public void shouldSuccesfulyGenerateEnergyConsumption() {
        Windmill windmill = new Windmill("some", true,150, 25);
        windmillService.saveWindmill(windmill);
        windmillService.simulateEnergyGeneration();
        BigDecimal newValue = windmillService.getAllWindmills().getLast().getTotalGenerated();
        assertTrue(newValue.compareTo(BigDecimal.ZERO) > 0);
        System.out.println(newValue);
    }

    @Test
    public void shouldSuccessfulyGenerateMeasurementForEnergyConsumption() {
        Windmill windmill = new Windmill("some",true, 120, 25);
        windmillService.saveWindmill(windmill);
        windmillService.simulateEnergyGeneration();
        List<GeneratedEnergyMeasure> measures = generatedMeasureRepository.findAll();
        assertEquals(1, measures.size());
        assertEquals(measures.getFirst().getTimestamp().getDayOfYear(), LocalDate.now().getDayOfYear());
        BigDecimal totalConsumed = windmillService.getAllWindmills().getLast().getTotalGenerated();
        BigDecimal expected = BigDecimal.valueOf(measures.getFirst().getValue());
        assertEquals(Windmill.class, measures.getFirst().getDevice().getClass());
        assertEquals(0, totalConsumed.compareTo(expected));
    }

    @Test
    public void ShouldNotGenerateConsumptionForInactiveDevice() {
        Windmill windmill = new Windmill("some", false, 200, 25);
        windmillService.saveWindmill(windmill);
        windmillService.simulateEnergyGeneration();
        BigDecimal newValue = windmillService.getAllWindmills().getLast().getTotalGenerated();
        assertEquals(0, newValue.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldSuccessfulyNotGenerateMeasurementForInactiveDevice() {
        Windmill windmill = new Windmill("some",false, 150, 25);
        windmillService.saveWindmill(windmill);
        windmillService.simulateEnergyGeneration();
        List<GeneratedEnergyMeasure> measures = generatedMeasureRepository.findAll();
        assertEquals(0, measures.size());
    }
}
