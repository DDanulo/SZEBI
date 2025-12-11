package com.example.server.Alerts.services;


import com.example.server.Alerts.entities.Alert;
import com.example.server.Alerts.entities.AlertLevel;
import com.example.server.Alerts.simulation.SensorData;
import com.example.server.DataPrediction.api.ForecastService;
import com.example.server.DataPrediction.data.Forecast;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class AlertPredictionService  {

   private final ForecastService forecastService;
   private final AlertPublisher alertPublisher;
   private final AlertRuleEngine alertRuleEngine;
   private final AlertDataInput alertDataInput;


   public void checkForecast(){
       Forecast lastForecast = forecastService.getLatestForecast();


       if(lastForecast != null){
           SensorData sensorData = SensorData.builder()
                   .source("Moduł Prognozowania")
                   .metric("MWH")
                   .value((double)lastForecast.getForecastedUsage())
                   .timestamp(lastForecast.getForecastDate())
                   .build();



          // System.out.println("Dane z prognozowania nie były null, a wartość wynosiła: " + sensorData.value());
           alertDataInput.handleData(sensorData);
       }
       else{
         //  System.out.println("Dane prognozowania null");
       }

   }
}
