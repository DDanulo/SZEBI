package com.example.server.Simulation.simulators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TimeOfDay {

    EARLY_MORNING("early morning",1, 0.5f), //4-8
    MORNING("morning",2,2), //8-12
    AFTERNOON("afternoon", 2.5f, 4), //12-16
    EVENING("evening",2, 2.5f), //16-20
    LATE_EVENING("late evening", 2, 0.25f), //20-24
    NIGHT("night",1.5f, 0.0f); //00.00 -4

    private final String value;
    final float temperatureCoefficient;
    final float insolationCoefficient;

    TimeOfDay(String value, float temperatureCoefficient, float insolationCoefficient) {
        this.value = value;
        this.temperatureCoefficient = temperatureCoefficient;
        this.insolationCoefficient = insolationCoefficient;
    }

    public TimeOfDay next() {
        TimeOfDay[] vals = values();
        return vals[(this.ordinal() + 1) % vals.length];
    }

    @JsonValue
    public String getValue() {
        return value;
    }
    @JsonCreator
    public static TimeOfDay fromValue(String value) {
        for (TimeOfDay timeOfDay : TimeOfDay.values()) {
            if (timeOfDay.value.equalsIgnoreCase(value)) {
                return timeOfDay;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid TimeOfDay value");
    }
}
