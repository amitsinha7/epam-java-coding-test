package com.qatarairways.adapter.flight.services;

import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchResponse;
import com.qatarairways.adapter.flight.enums.SortBy;
import com.qatarairways.adapter.flight.exceptions.InvalidInputException;
import com.qatarairways.adapter.flight.services.impl.FlightSearchServiceImpl;
import com.qatarairways.adapter.flight.views.FlightSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlightSearchServiceTests {
    private final static String airlineCode = "QATAR";
    private final static Date deptTime = new Date();
    @InjectMocks
    private FlightSearchServiceImpl flightSearchService;

    @BeforeEach
    public void setUp() {
        flightSearchService = new FlightSearchServiceImpl(
                request -> getFlightSummariesRandom());
    }

    @Test
    void fetchFlightsBasedOnRequestDefault() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 1, null, null,
                0L, 0, 5, SortOrder.UNSORTED);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsFilterByTimeTakenDesc() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 1, null, SortBy.DURATION,
                0L, 0, 5, SortOrder.DESCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsFilterByTimeTakenAsc() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 1, null, SortBy.DURATION,
                0L, 0, 4, SortOrder.ASCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        searchResponses.forEach(System.out::println);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsFilterByDurationDescAndConfirmFlight() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 1, false, SortBy.DURATION,
                0L, 0, 24, SortOrder.DESCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsFilterByDurationDescAndPossibleCancellationFlight() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 3, true, SortBy.DURATION,
                0L, 0, 5, SortOrder.DESCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsByAvgPriceAndDescAndConfirmFlight() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 1, false, SortBy.PRICE,
                2070L, 0, 5, SortOrder.DESCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightsFilterByAvgPriceAscAndPossibleCancellationFlight() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()), 3, true, SortBy.DURATION,
                5000L, 0, 5, SortOrder.ASCENDING);

        Collection<FlightSearchResponse> searchResponses = flightSearchService.fetchFlightsBasedOnRequest(request);
        assertEquals(searchResponses.size(), request.getSize());
    }

    @Test
    void fetchFlightSearchRequestInvalidInputException() {
        InvalidInputException invalidInputException = assertThrows(InvalidInputException.class,
                () -> flightSearchService.fetchFlightsBasedOnRequest(null));

        assertEquals("flight search can't we null", invalidInputException.getMessage());
    }

    private Collection<FlightSummary> getFlightSummariesRandom() {
        List<FlightSummary> summaryCollections = new ArrayList<>();
        long min = 10;
        float avgPrice = 1000;
        boolean isCancellationPossible = false;
        for (int i = 0; i < 8000; i++) {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(min, ChronoUnit.MINUTES));
            Date arrTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            FlightSummary summary = new FlightSummary(airlineCode, deptTime, arrTime, avgPrice, isCancellationPossible);
            summaryCollections.add(summary);
            min = min + 10;
            avgPrice = avgPrice + 5;
            if (isCancellationPossible) {
                isCancellationPossible = false;
                avgPrice = avgPrice - 6;
                min = min - 11;
            } else {
                isCancellationPossible = true;
            }
        }
        return summaryCollections;
    }
}
