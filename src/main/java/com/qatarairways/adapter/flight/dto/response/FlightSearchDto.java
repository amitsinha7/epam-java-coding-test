package com.qatarairways.adapter.flight.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class FlightSearchDto {
    /**
     * The airline code.
     */
    private final String airlineCode;
    /**
     * The departure datetime of the flight.
     */
    private final Date departureTime;
    /**
     * The expected arrival datetime of the flight.
     */
    private final Date arrivalTime;
    /**
     * The average price of the seats on this flight.
     */
    private final float averagePriceInUsd;
    /**
     * Whether cancellation is possible for this flight.
     */
    private final boolean cancellationPossible;
    /**
     * Difference between arrivalTime and departureTime
     */
    private final long duration;
}
