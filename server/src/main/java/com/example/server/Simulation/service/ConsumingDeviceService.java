package com.example.server.Simulation.service;


import com.example.server.Simulation.data.ConsumingDeviceRepository;
import com.example.server.Simulation.data.ConsumingMeasureRepository;
import com.example.server.Simulation.entities.ConsumedEnergyMeasure;
import com.example.server.Simulation.entities.ConsumingDevice;
import com.example.server.Simulation.exceptions.DeviceNotFoundException;
import com.example.server.Simulation.simulators.BasicSimulator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



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

    @Scheduled(fixedRate = 300_000, initialDelay = 10000)
    public void simulateEnergyConsumption() {
        double temperature = simulator.getTemperature();
        consumingDeviceRepository.findByWorkingTrue().forEach(device -> consumingMeasureRepository
                .save(new ConsumedEnergyMeasure(LocalDateTime.now(),device.consumeEnergy(temperature), device)));
    }
}
