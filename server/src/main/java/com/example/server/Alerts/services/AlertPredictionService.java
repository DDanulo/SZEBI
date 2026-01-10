package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.entities.Metric;
import com.example.server.Alerts.simulation.SensorData;
import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AlertPredictionService  {

   private final ForecastService forecastService;
   private final AlertPublisher alertPublisher;
   private final AlertRuleEngine alertRuleEngine;
   private final AlertDataInput alertDataInput;

   private LocalDateTime lastProcessedForecastTime;


   @Scheduled(initialDelay = 4000, fixedRate = 10000)
   public void checkForecast(){
       try{
        Forecast lastForecast = forecastService.getLatestForecast();
        if(lastForecast == null){
            return;
        }

        LocalDateTime forecastTime = lastForecast.getForecastDate();

        if(lastProcessedForecastTime != null && lastProcessedForecastTime.equals(forecastTime)){
            return;
        }

        lastProcessedForecastTime = forecastTime;

           SensorData sensorData = SensorData.builder()
                   .source("Moduł Prognozowania")
                   .metric(Metric.MWH)
                   .value((double)lastForecast.getForecastedUsage())
                   .timestamp(lastForecast.getForecastDate())
                   .build();

           alertDataInput.handleData(sensorData);

       }catch (Exception e){
           log.debug("Bład podczas pobierania prognozy");
       }


   }
}
