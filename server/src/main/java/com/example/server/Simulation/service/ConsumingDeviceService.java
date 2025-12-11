package com.example.server.Simulation.service;


import com.example.server.Simulation.data.ConsumingDeviceRepository;
import com.example.server.Simulation.data.ConsumingMeasureRepository;
import com.example.server.Simulation.entities.ConsumedEnergyMeasure;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class ConsumingDeviceService {

    private final ConsumingDeviceRepository consumingDeviceRepository;

    private final BasicSimulator  simulator;

    private final ConsumingMeasureRepository consumingMeasureRepository;

    public ConsumingDeviceService(ConsumingDeviceRepository consumingDeviceRepository, BasicSimulator simulator, ConsumingMeasureRepository consumingMeasureRepository) {
        this.consumingDeviceRepository = consumingDeviceRepository;
        this.simulator = simulator;

        this.consumingMeasureRepository = consumingMeasureRepository;
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

    public void activateConsumingDevice(UUID id){
        consumingDeviceRepository.findById(id).ifPresentOrElse(ConsumingDevice::activate, () -> {
            throw new DeviceNotFoundException("Consuming device with id: " + id + "not found");
        });
    }

    public void deactivateConsumingDevice(UUID id){
        consumingDeviceRepository.findById(id).ifPresentOrElse(ConsumingDevice::deactivate,  () -> {throw
                new DeviceNotFoundException("Consuming device with id: " + id + "not found");});
    }

    public ConsumingDevice deleteConsumingDevice(UUID id){
        var maybeConsumingDevice = consumingDeviceRepository.findById(id);
        if(maybeConsumingDevice.isEmpty()){
            throw new DeviceNotFoundException("Consuming device with id: " + id + "not found");
        }
        consumingDeviceRepository.deleteById(id);
        return maybeConsumingDevice.get();
    }

    @Scheduled(fixedRate = 300_000, initialDelay = 10000)
    public void simulateEnergyConsumption() {
        double temperature = simulator.getTemperature();
        consumingDeviceRepository.findAll().forEach(device -> consumingMeasureRepository
                .save(new ConsumedEnergyMeasure(LocalDateTime.now(),device.consumeEnergy(temperature), device)));
    }
}
