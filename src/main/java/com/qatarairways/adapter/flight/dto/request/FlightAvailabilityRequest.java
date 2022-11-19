package com.qatarairways.adapter.flight.dto.request;

import java.util.Date;

import lombok.NonNull;
import lombok.Value;

/**
 * Represents a flight availability request.
 */
@Value
public class FlightAvailabilityRequest {
    /**
     * The origin (departure) of the flights.
     */
    @NonNull
    String origin;
    /**
     * The target destination of the flights.
     */
    @NonNull
    String destination;
    /**
     * The day of departure.
     */
    @NonNull
    Date departureDate;
    /**
     * The required number of seats.
     */
    int numberOfTravellers;

}
