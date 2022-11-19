package com.qatarairways.adapter.flight.services.impl;

import com.qatarairways.adapter.flight.dto.request.FlightAvailabilityRequest;
import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchDto;
import com.qatarairways.adapter.flight.enums.FlightSort;
import com.qatarairways.adapter.flight.enums.FlightStatus;
import com.qatarairways.adapter.flight.exceptions.InvalidInputException;
import com.qatarairways.adapter.flight.services.FlightAvailabilityService;
import com.qatarairways.adapter.flight.services.FlightSearchService;
import com.qatarairways.adapter.flight.views.FlightSummary;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        Timestamp deptTimeStamp = new Timestamp(Long.parseLong(request.getDepartureDateTime()));
        Date deptDate = new Date(deptTimeStamp.getTime());

        FlightAvailabilityRequest req = new FlightAvailabilityRequest(request.getOrigin(),
                request.getDestination(), deptDate, request.getNumberOfTravellers());

        Collection<FlightSummary> flightSummaries = flightAvailabilityService.getAvailableFlights(req);

        if (flightSummaries != null && !flightSummaries.isEmpty()) {
            if (request.getFlightStatus() == FlightStatus.NA && request.getMaxPriceInUsd() == 0L
                    && request.getFlightSort() == FlightSort.DURATION && request.getOrder().equals("ASC")) {
                flightSummaries.forEach(flightSummary -> {
                    long dur = flightSummary.getArrivalTime().getTime()-flightSummary.getDepartureTime().getTime();
                    FlightSearchDto flightSearchDto = new FlightSearchDto();
                    durationMap.put(dur, flightSummary);
                });
                Set<Long> duration = durationMap.keySet();
                duration.t

            }
        }


        return null;
    }
}
