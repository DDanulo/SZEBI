package com.example.server.Simulation.simulators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Component("basic")
public class BasicSimulator implements Simulator {

    @Setter
    @Getter
    private TimeOfDay dayTime;
    @Getter
    @Setter
    private Season season;
    //in Celsius
    @Getter
    private volatile double temperature;
    //in m/s
    @Getter
    private volatile double windSpeed;
    //W/m ( per hour)
    @Getter
    private volatile float insolation;
    private int counter;


    public BasicSimulator() {
        dayTime = TimeOfDay.EARLY_MORNING;
        season = Season.SPRING;
        temperature = 8.0f;
        windSpeed = 0f;
        insolation = 5.1f;
        counter = 0; //4:00 in the morning
    }

    @Override
    public void simulate() {
        this.insolation = 135.0f *this.dayTime.insolationCoefficient *this.season.insolationCoefficient;
        this.temperature = 8.0f * this.dayTime.temperatureCoefficient * this.season.temperatureCoefficient;
        this.windSpeed = Math.random() * 5 * 3.1f * this.season.windSpeedCoefficient;
        if(++counter ==48) {
            dayTime = dayTime.next();
            counter = 0;
        }

    }

    @Override
    public Map<String, Object> getSimulationParameters() {
        Map<String, Object> map = new HashMap<>();
        map.put("dayTime", dayTime.toString());
        map.put("season", season.toString());
        map.put("temperature", temperature);
        map.put("windSpeed", windSpeed);
        map.put("insolation", insolation);
        map.put("counter", counter);
        return map;
    }
}
