package com.example.server.Simulation.simulators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Season {
    SPRING("spring",3, 3, 3),
    SUMMER("summer",5, 2, 4),
    AUTUMN("autumn",2,3, 2 ),
    WINTER("winter",1, 4, 1)
    ;
    final String value;
    final int insolationCoefficient;
    final int windSpeedCoefficient;
    final int temperatureCoefficient;


    Season(String value, int insolationCoefficient, int windSpeedCoefficient, int temperatureCoefficient) {
        this.value = value;
        this.insolationCoefficient = insolationCoefficient;
        this.windSpeedCoefficient = windSpeedCoefficient;
        this.temperatureCoefficient = temperatureCoefficient;
    }

    public Season next() {
        Season[] vals = values();
        return vals[(this.ordinal() + 1) % vals.length];
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Season fromValue(String value) {
        for (Season season : Season.values()) {
            if (season.value.equalsIgnoreCase(value)) {
                return season;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid Season value");
    }
}
