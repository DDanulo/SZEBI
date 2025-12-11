package com.example.server.Simulation.service;


import com.example.server.Simulation.data.SolarPanelRepository;
import com.example.server.Simulation.entities.EnergyProducingDevice;
import com.example.server.Simulation.entities.SolarPanel;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import com.example.server.Simulation.simulators.Simulator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Component
public class SolarPanelService {


    private final BasicSimulator simulator;

    private final Thread simulationThread;

    private final SolarPanelRepository solarPanelRepository;

    public SolarPanelService(BasicSimulator simulator, SolarPanelRepository solarPanelRepository) {
        this.simulator = simulator;
        this.solarPanelRepository = solarPanelRepository;
        this .simulationThread = new Thread(() -> {
            while (true) {
                simulateEnergyGeneration();
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @PostConstruct
    public void init() {
        simulationThread.start();
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
        return  solarPanelRepository.findAll();
    }

    public SolarPanel deleteSolarPanel(UUID id) {
        var maybeSolarPanel = solarPanelRepository.findById(id);
        if (maybeSolarPanel.isEmpty()) {
            throw new DeviceNotFoundException("SolarPanel with id " + id + " not found");
        }
       solarPanelRepository.deleteById(id);
        return  solarPanelRepository.findById(id).get();
    }

    private void simulateEnergyGeneration() {
        double insolation = simulator.getInsolation();
        solarPanelRepository.findAll().forEach(solarPanel -> {solarPanel.generateEnergy(insolation);});
    }




}
