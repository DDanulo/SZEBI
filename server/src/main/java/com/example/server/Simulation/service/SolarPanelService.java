package com.example.server.Simulation.service;


import com.example.server.Simulation.data.GeneratedMeasureRepository;
import com.example.server.Simulation.data.SolarPanelRepository;
import com.example.server.Simulation.entities.EnergyProducingDevice;
import com.example.server.Simulation.entities.GeneratedEnergyMeasure;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import com.example.server.Simulation.simulators.Simulator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Component
public class SolarPanelService {


    private final BasicSimulator simulator;

    private final SolarPanelRepository solarPanelRepository;

    private final GeneratedMeasureRepository generatedMeasureRepository;

    public SolarPanelService(BasicSimulator simulator, SolarPanelRepository solarPanelRepository, GeneratedMeasureRepository generatedMeasureRepository) {
        this.simulator = simulator;
        this.solarPanelRepository = solarPanelRepository;
        this.generatedMeasureRepository = generatedMeasureRepository;
    }


    public SolarPanel addSolarPanel(SolarPanel solarPanel) {
        return solarPanelRepository.save(solarPanel);
    }

    public Optional<SolarPanel> getSolarPanel(UUID id) {
        return solarPanelRepository.findById(id);
    }

    public void activateSolarPanel(UUID id) {
        SolarPanel solarPanel = solarPanelRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("SolarPanel with id " + id + " not found"));

        solarPanel.activate();
        solarPanelRepository.save(solarPanel);
    }

    public void deactivateSolarPanel(UUID id) {
        SolarPanel solarPanel = solarPanelRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("SolarPanel with id " + id + " not found"));

        solarPanel.deactivate();
        solarPanelRepository.save(solarPanel);
    }

    public List<SolarPanel> getAllSolarPanels() {
        return solarPanelRepository.findAll();
    }

    public SolarPanel deleteSolarPanel(UUID id) {
        var maybeSolarPanel = solarPanelRepository.findById(id);
        if (maybeSolarPanel.isEmpty()) {
            throw new DeviceNotFoundException("SolarPanel with id " + id + " not found");
        }
        solarPanelRepository.deleteById(id);
        return maybeSolarPanel.get();
    }

    @Transactional
    @Scheduled(fixedRate = 300_000, initialDelay = 10000)
    public void simulateEnergyGeneration() {
        double insolation = simulator.getInsolation();
        solarPanelRepository.findByWorkingTrue().forEach(solarPanel -> generatedMeasureRepository.save(new GeneratedEnergyMeasure(LocalDateTime.now(),
                solarPanel.generateEnergy(insolation), solarPanel)));
    }


}
