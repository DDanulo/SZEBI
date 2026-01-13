package com.example.server.Simulation.simulators;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Component("basic")
public class BasicSimulator implements Simulator {


    @Getter
    private TimeOfDay dayTime;
    @Getter
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

    private int seasonCounter;


    public BasicSimulator() {
        dayTime = TimeOfDay.EARLY_MORNING;
        season = Season.SPRING;
        temperature = 8.0f;
        windSpeed = 0f;
        insolation = 5.1f;
        counter = 0; //4:00 in the morning
        seasonCounter = 0;
    }

    @Override
    public void simulate() {
        this.insolation = 135.0f *this.dayTime.insolationCoefficient *this.season.insolationCoefficient;
        this.temperature = 4.0f * this.dayTime.temperatureCoefficient * this.season.temperatureCoefficient;
        this.windSpeed = Math.random() * 3.1f * this.season.windSpeedCoefficient;
        if(this.season == Season.WINTER) {
            this.temperature -=10.0f;
        }
        if(++counter ==49) {
            dayTime = dayTime.next();
            counter = 0;
        }
        if(++seasonCounter ==92) {
            season = season.next();
            seasonCounter = 0;
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

    void changeTimeOfDay(TimeOfDay timeOfDay) {
        this.dayTime = timeOfDay;
        counter = 0;
        simulate();
    }

    void changeSeason (Season season) {
        this.season = season;
        seasonCounter = 0;
        simulate();
    }

}
