package com.example.server.DeviceControl;

import com.example.server.Simulation.api.IControlDevices;
import com.example.server.Simulation.api.IShowDevices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleExecutor {

    private final ScheduleRepo scheduleRepo;
    private final IControlDevices controlDevices;
    private final IShowDevices showDevices;


    @Scheduled(fixedRate = 3000)
    public void executeSchedules() {
        List<Schedule> allSchedules = scheduleRepo.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Schedule s : allSchedules) {


            if (isTriggerTime(s.getDateTimeTurnOn(), now)) {
                turnOnIfOff(s.getDeviceId());
            }


            if (isTriggerTime(s.getDateTimeTurnOff(), now)) {
                turnOffIfOn(s.getDeviceId());
            }
        }
    }


    private boolean isTriggerTime(LocalDateTime targetTime, LocalDateTime now) {

        if (now.isBefore(targetTime)) {
            return false;
        }


        long secondsDiff = Duration.between(targetTime, now).getSeconds();


        return secondsDiff < 10;
    }

    private void turnOnIfOff(UUID id) {
        showDevices.getConsumingDevice(id).ifPresent(d -> {
            if (!d.isWorking()) {

                controlDevices.activateConsumingDevice(id);
            }
        });
        showDevices.getWindmill(id).ifPresent(d -> {
            if (!d.isWorking()) {

                controlDevices.activateWindmill(id);
            }
        });
        showDevices.getSolarPanel(id).ifPresent(d -> {
            if (!d.isWorking()) {

                controlDevices.activateSolarPanel(id);
            }
        });

    }

    private void turnOffIfOn(UUID id) {
        showDevices.getConsumingDevice(id).ifPresent(d -> {
            if (d.isWorking()) {
                controlDevices.deactivateConsumingDevice(id);
            }
        });
        showDevices.getSolarPanel(id).ifPresent(d -> {
            if (d.isWorking()) {
                controlDevices.deactivateSolarPanel(id);
            }
        });
        showDevices.getWindmill(id).ifPresent(d -> {
            if (d.isWorking()) {
                controlDevices.deactivateWindmill(id);
            }
        });
    }
}