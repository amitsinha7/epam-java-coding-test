package com.qatarairways.adapter.flight.enums;

public enum FlightSort {
    DURATION("cancellation"),
    PRICE("available");
    private String sort;

    FlightSort(String sort) {
        this.sort = sort;
    }

    public String flightStatus() {
        return sort;
    }
}
