package com.example.server.simulation;


import com.example.server.Simulation.data.GeneratedMeasureRepository;
import com.example.server.Simulation.entities.GeneratedEnergyMeasure;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.entities.Windmill;
import com.example.server.Simulation.service.SolarPanelService;
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

@Transactional
@SpringBootTest
@Testcontainers
public class SolarPanelGenerationTest {

    @Autowired
    private SolarPanelService  solarPanelService;

    @Autowired
    private GeneratedMeasureRepository generatedMeasureRepository;


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
        SolarPanel solarPanel = new SolarPanel("some",true, 5);
        solarPanelService.addSolarPanel(solarPanel);
        solarPanelService.simulateEnergyGeneration();
        BigDecimal newValue = solarPanelService.getAllSolarPanels().getLast().getTotalGenerated();
        assertTrue(newValue.compareTo(BigDecimal.ZERO) > 0);
        System.out.println(newValue);
    }

    @Test
    public void shouldSuccessfulyGenerateMeasurementForEnergyConsumption() {
        SolarPanel solarPanel = new SolarPanel("some",true, 520);
        solarPanelService.addSolarPanel(solarPanel);
        solarPanelService.simulateEnergyGeneration();
        List<GeneratedEnergyMeasure> measures = generatedMeasureRepository.findAll();
        assertEquals(1, measures.size());
        assertEquals(measures.getFirst().getTimestamp().getDayOfYear(), LocalDate.now().getDayOfYear());
        BigDecimal totalConsumed = solarPanelService.getAllSolarPanels().getLast().getTotalGenerated();
        BigDecimal expected = BigDecimal.valueOf(measures.getFirst().getValue());
        assertEquals(SolarPanel.class, measures.getFirst().getDevice().getClass());
        assertEquals(0, totalConsumed.compareTo(expected));
    }

    @Test
    public void ShouldNotGenerateConsumptionForInactiveDevice() {
        SolarPanel solarPanel = new SolarPanel("some", false, 5);
        solarPanelService.addSolarPanel(solarPanel);
        solarPanelService.simulateEnergyGeneration();
        BigDecimal newValue = solarPanelService.getAllSolarPanels().getLast().getTotalGenerated();
        assertEquals(0, newValue.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldSuccessfulyNotGenerateMeasurementForInactiveDevice() {
        SolarPanel solarPanel = new SolarPanel("some",false, 5);
        solarPanelService.addSolarPanel(solarPanel);
        solarPanelService.simulateEnergyGeneration();
        List<GeneratedEnergyMeasure> measures = generatedMeasureRepository.findAll();
        assertEquals(0, measures.size());
    }
}
