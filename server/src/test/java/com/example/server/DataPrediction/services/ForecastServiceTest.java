package com.example.server.DataPrediction.services;

import com.example.server.DataPrediction.data.UsageRecord;
import com.example.server.DataPrediction.repositories.ForecastRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ForecastServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("tester")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private ForecastRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("DROP TABLE IF EXISTS energy_measure");
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS energy_measure (" +
                "type VARCHAR(31) NOT NULL," +
                "timestamp TIMESTAMP WITHOUT TIME ZONE," +
                "value DECIMAL," +
                "device_id UUID," +
                "CONSTRAINT pk_energymeasure PRIMARY KEY (timestamp));");

        List<UsageRecord> data = ForecasterModel.generateMockUsageData(14);

        List<Object[]> batchArgs = data.stream()
                .map(record -> new Object[]{
                        Timestamp.valueOf(record.timestamp), // Konwersja na typ SQL
                        record.value
                })
                .collect(Collectors.toList());

        // C. Szybki wsad do bazy (Batch Insert)
        // Upewnij się, że nazwy kolumn (timestamp, value) pasują do Twojej tabeli w bazie!
        String sql = "INSERT INTO energy_measure (type, timestamp, value) VALUES ('consumed', ?, ?)";

        jdbcTemplate.batchUpdate(sql, batchArgs);

        System.out.println("Wstawiono " + batchArgs.size() + " rekordów do bazy testowej.");
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE energy_measure");
        jdbcTemplate.update("TRUNCATE TABLE forecasts");
    }

    @Test
    public void trainModelTest() {
        ForecastServiceImpl service = new ForecastServiceImpl(repository);

        System.out.println(repository.fetchUsages(LocalDateTime.now().minusDays(14L), LocalDateTime.now()));

        service.trainModel();

        assertNotNull(service.getModel());
        assertNotNull(service.getModel().getLastTrainingTime());

        assertTrue(service.getModel().getLastTrainingTime().isBefore(Instant.now()));
    }

    @Test
    public void createForecastTest() {
        ForecastServiceImpl service = new ForecastServiceImpl(repository);

        service.trainModel();

        assertEquals(0, service.getLatestForecasts().size());

        service.generateForecast();

        System.out.println(service.getLatestForecasts());
        assertEquals(8, service.getLatestForecasts().size());
    }

    @Test
    public void getForecastsTest() {
        ForecastServiceImpl service = new ForecastServiceImpl(repository);

        service.trainModel();

        service.generateDailyForecasts();

        System.out.println(service.getForecasts(Instant.now().minusSeconds(60), Instant.now()));
        assertEquals(8, service.getForecasts(Instant.now().minusSeconds(60), Instant.now()).size());
    }
}
