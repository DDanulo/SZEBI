package com.example.server.Simulation.service;


import com.example.server.Simulation.data.SolarPanelRepository;
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
    private void init() {
        simulationThread.start();
    }

    public SolarPanel addSolarPanel(boolean active, double area) {
        return solarPanelRepository.save(new SolarPanel(active, area));
    }

    public Optional<SolarPanel> getSolarPanel(UUID id) {
        return solarPanelRepository.findById(id);
    }

    public void activateSolarPanel(UUID id) {
        solarPanelRepository.findById(id).ifPresentOrElse(solarPanel -> {solarPanel.setWorking(true);},
                () -> {throw new DeviceNotFoundException("SolarPanel with id " + id + " not found");});
    }

    public void deactivateSolarPanel(UUID id) {
        solarPanelRepository.findById(id).ifPresentOrElse(solarPanel -> {solarPanel.setWorking(false);},
                () -> {throw new DeviceNotFoundException("SolarPanel with id " + id + " not found");});
    }

    public List<SolarPanel> getAllSolarPanels() {
        return  solarPanelRepository.findAll();
    }

    public SolarPanel delete(UUID id) {
        return  solarPanelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SolarPanel with id " + id + " not found"));
    }

    private void simulateEnergyGeneration() {
        double insolation = simulator.getInsolation();
        solarPanelRepository.findAll().forEach(solarPanel -> {solarPanel.generateEnergy(insolation);});
    }




}
