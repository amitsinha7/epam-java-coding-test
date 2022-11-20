package com.qatarairways.adapter.flight.services;

import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchResponse;

import java.util.Collection;

public interface FlightSearchService {

    /**
     * Returns all flights matching the request.
     * The flights returned are the ones which are going from {@code origin} to {@code destination} on the date
     * {@code departureDate}.
     * It is also ensured that the flight has at least {@code numberOfTravellers} seats available.
     *
     * @param request  the search request
     * pageable is for sorting and limiting the result set.
     *                 As Page dependency is unavailable, Returning Collection
     * @return collection of matching flight details
     */
    Collection<FlightSearchResponse> fetchFlightsBasedOnRequest(FlightSearchRequest request);
}
