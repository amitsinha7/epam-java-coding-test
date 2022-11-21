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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
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

        List<FlightSearchResponse> searchResponses = new ArrayList<>();

        if (flightSummaries != null && !flightSummaries.isEmpty()) {

            logger.debug("After calling flightAvailabilityService.getAvailableFlights and flightSummaries size as {}",
                    flightSummaries.size());

            flightSummaries.forEach(flightSummary -> {

                long dur = flightSummary.getArrivalTime().getTime() - flightSummary.getDepartureTime().getTime();

                FlightSearchResponse flightSearchDto = new FlightSearchResponse(flightSummary.getAirlineCode(),
                        flightSummary.getDepartureTime(), flightSummary.getArrivalTime(),
                        flightSummary.getAveragePriceInUsd(), flightSummary.isCancellationPossible(), dur);

                searchResponses.add(flightSearchDto);
            });

        }

        if (searchResponses.isEmpty()) {
            return Collections.emptyList();
        } else {
            return filterBasedFlightDetails(searchResponses, request);
        }
    }

    private Collection<FlightSearchResponse> filterBasedFlightDetails(List<FlightSearchResponse> searchResponses,
                                                                      FlightSearchRequest request) {

        logger.debug("Inside filterBasedFlightDetails and flightSearchDtoList size as {}", searchResponses.size());

        //Default predicate just checking departure time lies within a year
        Predicate<FlightSearchResponse> predicates = getPredicatesForFlightSearchRequest(request);

        //Default filter based on duration
        Comparator<FlightSearchResponse> sorted =
                getComparatorBasedOnSearchRequest(request.getOrder(), request.getSortBy());

        return searchResponses.stream()
                .limit(request.getSize())
                .sorted(sorted)
                .filter(predicates)
                .collect(Collectors.toList());
    }

    private Comparator<FlightSearchResponse> getComparatorBasedOnSearchRequest(SortOrder order, SortBy sortBy) {
        //Default filter based on departure time
        Comparator<FlightSearchResponse> searchResponseComparator =
                Comparator.comparing(FlightSearchResponse::getDepartureTime);
        if (sortBy == SortBy.DURATION) {
            if (order == SortOrder.DESCENDING) {
                return Comparator.comparing(FlightSearchResponse::getDuration).reversed();
            }
        } else if (sortBy == SortBy.PRICE) {
            if (order == SortOrder.ASCENDING) {
                return Comparator.comparing(FlightSearchResponse::getAveragePriceInUsd);
            } else if (order == SortOrder.DESCENDING) {
                return Comparator.comparing(FlightSearchResponse::getAveragePriceInUsd).reversed();
            }
        }
        return searchResponseComparator;
    }

    private Predicate<FlightSearchResponse> getPredicatesForFlightSearchRequest(FlightSearchRequest request) {

        if (request.getFlightStatus() != null) {
            return p -> p.isCancellationPossible() == request.getFlightStatus();
        }

        if (request.getMaxPriceInUsd() > 0) {
            return p -> p.getAveragePriceInUsd() <= request.getMaxPriceInUsd();
        }

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        return p -> p.getDepartureTime().compareTo(c.getTime()) >=0 ;
    }

}
