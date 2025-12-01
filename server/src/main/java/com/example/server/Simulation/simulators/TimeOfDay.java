package com.example.server.Simulation.simulators;

public enum TimeOfDay {

    EARLY_MORNING(1, 0.5f), //4-8
    MORNING(2,2), //8-12
    AFTERNOON(2.5f, 4), //12-16
    EVENING(2, 2.5f), //16-20
    LATE_EVENING(2, 0.25f), //20-24
    NIGHT(1.5f, 0.0f); //00.00 -4

    final float temperatureCoefficient;
    final float insolationCoefficient;

    TimeOfDay(float temperatureCoefficient, float insolationCoefficient) {
        this.temperatureCoefficient = temperatureCoefficient;
        this.insolationCoefficient = insolationCoefficient;
    }

    public TimeOfDay next() {
        TimeOfDay[] vals = values();
        return vals[(this.ordinal() + 1) % vals.length];
    }

}
