package com.example.server.Simulation.simulators;

public enum Season {
    SPRING(3, 3, 3),
    SUMMER(5, 2, 4),
    AUTUMN(2,3, 2 ),
    WINTER(1, 4, 1)
    ;
    final int insolationCoefficient;
    final int windSpeedCoefficient;
    final int temperatureCoefficient;


    Season(int insolationCoefficient, int windSpeedCoefficient, int temperatureCoefficient) {
        this.insolationCoefficient = insolationCoefficient;
        this.windSpeedCoefficient = windSpeedCoefficient;
        this.temperatureCoefficient = temperatureCoefficient;
    }

    public Season next() {
        Season[] vals = values();
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
