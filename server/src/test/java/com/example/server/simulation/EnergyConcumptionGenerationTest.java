package com.example.server.simulation;


import com.example.server.Simulation.data.ConsumingMeasureRepository;
import com.example.server.Simulation.entities.ConsumedEnergyMeasure;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.service.ConsumingDeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
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
public class EnergyConcumptionGenerationTest {

    @Autowired
    private ConsumingDeviceService consumingDeviceService;

    @Autowired
    private ConsumingMeasureRepository consumingMeasureRepository;

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
    void contextLoads() {
    }

    @Test
    public void shouldSuccesfulyGenerateEnergyConsumption() {
        ConsumingDevice consumingDevice = new ConsumingDevice("some consuming device", true, 12d);
        consumingDeviceService.addConsumingDevice(consumingDevice);
        consumingDeviceService.simulateEnergyConsumption();
        BigDecimal newValue = consumingDeviceService.getAllConsumingDevices().getLast().getTotalConsumed();
        assertTrue(newValue.compareTo(BigDecimal.ZERO) > 0);
        System.out.println(newValue);
    }

    @Test
    public void shouldSuccessfulyGenerateMeasurementForEnergyConsumption() {
        ConsumingDevice consumingDevice = new ConsumingDevice("some another funky consuming device", true, 12d);
        consumingDeviceService.addConsumingDevice(consumingDevice);
        consumingDeviceService.simulateEnergyConsumption();
        List<ConsumedEnergyMeasure> measures = consumingMeasureRepository.findAll();
        assertEquals(1, measures.size());
        assertEquals(measures.getFirst().getTimestamp().getDayOfYear(), LocalDate.now().getDayOfYear());
        BigDecimal totalConsumed = consumingDeviceService.getAllConsumingDevices().getLast().getTotalConsumed();
        BigDecimal expected = BigDecimal.valueOf(measures.getFirst().getValue());
        assertEquals(0, totalConsumed.compareTo(expected));
    }

    @Test
    public void ShouldNotGenerateConsumptionForInactiveDevice() {
        ConsumingDevice consumingDevice = new ConsumingDevice("some bad device", false, 12d);
        consumingDeviceService.addConsumingDevice(consumingDevice);
        consumingDeviceService.simulateEnergyConsumption();
        BigDecimal newValue = consumingDeviceService.getAllConsumingDevices().getLast().getTotalConsumed();
        assertEquals(0, newValue.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldSuccessfulyNotGenerateMeasurementForInactiveDevice() {
        ConsumingDevice consumingDevice = new ConsumingDevice("some another bad consuming device", false, 12d);
        consumingDeviceService.addConsumingDevice(consumingDevice);
        consumingDeviceService.simulateEnergyConsumption();
        List<ConsumedEnergyMeasure> measures = consumingMeasureRepository.findAll();
        assertEquals(0, measures.size());
    }
}
