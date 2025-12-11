package com.example.server.Simulation.service;


import com.example.server.Simulation.data.ConsumingDeviceRepository;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class ConsumingDeviceService {

    private final ConsumingDeviceRepository consumingDeviceRepository;

    private final Thread simulationThread;

    private final BasicSimulator  simulator;

    public ConsumingDeviceService(ConsumingDeviceRepository consumingDeviceRepository, BasicSimulator simulator) {
        this.consumingDeviceRepository = consumingDeviceRepository;
        this.simulator = simulator;
        this.simulationThread = new Thread(() ->
        {while(true){
            simulateEnergyConsumption();
            try {
                sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        });
    }

    public ConsumingDevice addConsumingDevice(ConsumingDevice consumingDevice) {
        return consumingDeviceRepository.save(consumingDevice);
    }

    public List<ConsumingDevice> getAllConsumingDevices(){
        return consumingDeviceRepository.findAll();
    }

    public Optional<ConsumingDevice> getConsumingDevice(UUID id){
        return  consumingDeviceRepository.findById(id);
    }

    public void activateConsumingDevice(UUID id) {
        ConsumingDevice device = consumingDeviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Consuming device with id: " + id + " not found"));

         device.activate();

          consumingDeviceRepository.save(device);
    }

    public void deactivateConsumingDevice(UUID id) {
        ConsumingDevice device = consumingDeviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Consuming device with id: " + id + " not found"));

        device.deactivate();

         consumingDeviceRepository.save(device);
    }

    public ConsumingDevice deleteConsumingDevice(UUID id){
        var maybeConsumingDevice = consumingDeviceRepository.findById(id);
        if(maybeConsumingDevice.isEmpty()){
            throw new DeviceNotFoundException("Consuming device with id: " + id + "not found");
        }
        consumingDeviceRepository.deleteById(id);
        return maybeConsumingDevice.get();
    }

    @PostConstruct
    public void init(){
        simulationThread.start();
    }
    private void simulateEnergyConsumption() {
        double temperature = simulator.getTemperature();
        consumingDeviceRepository.findAll().forEach(device -> {device.consumeEnergy(temperature);});
    }
}
