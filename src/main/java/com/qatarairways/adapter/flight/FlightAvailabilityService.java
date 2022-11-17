package com.qatarairways.adapter.flight;

import java.util.Collection;

/**
 * Provides flight availability information.
 */
public interface FlightAvailabilityService {
    /**
     * Returns all flights matching the request.
     * The flights returned are the ones which are going from {@code origin} to {@code destination} on the date {@code departureDate}.
     * It is also ensured that the flight has at least {@code numberOfTravellers} seats available.
     *
     * @param request the availability request
     * @return collection of matching flight details
     */
    Collection<FlightSummary> getAvailableFlights(FlightAvailabilityRequest request);
}
