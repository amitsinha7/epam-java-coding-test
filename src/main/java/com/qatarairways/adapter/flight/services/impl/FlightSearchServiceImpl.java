package com.qatarairways.adapter.flight.services.impl;

import com.qatarairways.adapter.flight.dto.request.FlightAvailabilityRequest;
import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchDto;
import com.qatarairways.adapter.flight.enums.FlightStatus;
import com.qatarairways.adapter.flight.exceptions.InvalidInputException;
import com.qatarairways.adapter.flight.services.FlightAvailabilityService;
import com.qatarairways.adapter.flight.services.FlightSearchService;
import com.qatarairways.adapter.flight.views.FlightSummary;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class FlightSearchServiceImpl implements FlightSearchService {

    private final FlightAvailabilityService flightAvailabilityService;

    public FlightSearchServiceImpl(FlightAvailabilityService flightAvailabilityService) {
        this.flightAvailabilityService = flightAvailabilityService;
    }

    /**
     * Returns all flights matching the request.
     * The flights returned are the ones which are going from {@code origin} to {@code destination} on the date
     * {@code departureDate}.
     * It is also ensured that the flight has at least {@code numberOfTravellers} seats available.
     *
     * @param request  the search request
     * @param pageable is for sorting and limiting the result set.
     *                 As Page dependency is unavailable, Returning Collection
     * @return collection of matching flight details
     */

    @Override
    public Collection<FlightSearchDto> fetchFlightsBasedOnRequest(FlightSearchRequest request) {

        validateRequest(request);

        Timestamp stamp = new Timestamp(Long.parseLong(request.getDepartureDate()));
        Date date = new Date(stamp.getTime());

        FlightAvailabilityRequest req = new FlightAvailabilityRequest(request.getOrigin(),
                request.getDestination(), date, request.getNumberOfTravellers());

        Collection<FlightSummary> flightSummaries = flightAvailabilityService.getAvailableFlights(req);

        if (flightSummaries != null && !flightSummaries.isEmpty()) {
            if (request.getFlightStatus() == FlightStatus.NA && request.getMaxPriceInUsd() == null) {

            }
        }


        return null;
    }


    private void validateRequest(FlightSearchRequest request) {
        if (request != null && Long.parseLong(request.getDepartureDate()) > Instant.now().toEpochMilli()) {
            throw new InvalidInputException("Bad Request");
        }
    }
}
