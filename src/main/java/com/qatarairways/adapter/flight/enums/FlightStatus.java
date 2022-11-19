package com.qatarairways.adapter.flight.enums;

public enum FlightStatus {
    CANCELLATION("cancellation"),
    AVAILABLE("available"),
    NA("na");
    private String name;

    FlightStatus(String flightStatus) {
        this.name = flightStatus;
    }

    public String flightStatus() {
        return name;
    }
}
