package com.qatarairways.adapter.flight.services.impl;

import com.qatarairways.adapter.flight.dto.request.FlightAvailabilityRequest;
import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchResponse;
import com.qatarairways.adapter.flight.enums.SortBy;
import com.qatarairways.adapter.flight.services.FlightAvailabilityService;
import com.qatarairways.adapter.flight.services.FlightSearchService;
import com.qatarairways.adapter.flight.views.FlightSummary;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FlightSearchServiceImpl implements FlightSearchService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
     * @param request the search request
     *                pageable is for sorting and limiting the result set.
     *                As Page dependency is unavailable, Returning Collection
     * @return collection of matching flight details
     */

    @Override
    public Collection<FlightSearchResponse> fetchFlightsBasedOnRequest(FlightSearchRequest request) {

        logger.debug("Inside fetchFlightsBasedOnRequest and request data as {}", request);

        Timestamp deptTimeStamp = new Timestamp(Long.parseLong(request.getDepartureDateTime()));
        Date deptDate = new Date(deptTimeStamp.getTime());

        FlightAvailabilityRequest req = new FlightAvailabilityRequest(request.getOrigin(), request.getDestination(),
                deptDate, request.getNumberOfTravellers());

        logger.debug("Calling flightAvailabilityService.getAvailableFlights and request send as {}", req);

        Collection<FlightSummary> flightSummaries = flightAvailabilityService.getAvailableFlights(req);

        List<FlightSearchResponse> flightSearchDtoList = new ArrayList<>();

        if (flightSummaries != null && !flightSummaries.isEmpty()) {

            logger.debug("After calling flightAvailabilityService.getAvailableFlights and flightSummaries size as {}",
                    flightSummaries.size());

            flightSummaries.forEach(flightSummary -> {
                long dur = flightSummary.getArrivalTime().getTime() - flightSummary.getDepartureTime().getTime();

                FlightSearchResponse flightSearchDto = new FlightSearchResponse(flightSummary.getAirlineCode(),
                        flightSummary.getDepartureTime(), flightSummary.getArrivalTime(),
                        flightSummary.getAveragePriceInUsd(), flightSummary.isCancellationPossible(), dur);

                flightSearchDtoList.add(flightSearchDto);
            });

        }

        if (flightSearchDtoList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return filterBasedFlightDetails(flightSearchDtoList, request);
        }
    }

    private Collection<FlightSearchResponse> filterBasedFlightDetails(List<FlightSearchResponse> flightSearchDtoList,
                                                                      FlightSearchRequest request) {

        logger.debug("Inside filterBasedFlightDetails and flightSearchDtoList size as {}", flightSearchDtoList.size());

        if (request.getSortBy() == SortBy.DURATION) {
            if (request.getOrder() == SortOrder.ASCENDING) {
                return flightSearchDtoList.stream()
                        .limit(request.getSize())
                        .sorted(Comparator.comparing(FlightSearchResponse::getDuration)).collect(Collectors.toList());
            } else {
                return flightSearchDtoList.stream()
                        .limit(request.getSize())
                        .sorted(Comparator.comparing(FlightSearchResponse::getDuration).reversed())
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

}
