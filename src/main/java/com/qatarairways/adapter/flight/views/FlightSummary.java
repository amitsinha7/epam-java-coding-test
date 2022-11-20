package com.qatarairways.adapter.flight.views;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Provides summary of a specific flight.
 */
@Value
@Builder
@AllArgsConstructor
public class FlightSummary {
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
}
