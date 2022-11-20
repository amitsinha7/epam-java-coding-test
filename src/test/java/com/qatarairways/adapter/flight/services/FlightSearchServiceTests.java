package com.qatarairways.adapter.flight.services;

import com.qatarairways.adapter.flight.dto.request.FlightAvailabilityRequest;
import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchDto;
import com.qatarairways.adapter.flight.services.impl.FlightSearchServiceImpl;
import com.qatarairways.adapter.flight.views.FlightSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FlightSearchServiceTests {
    private final static String airlineCode = "QATAR";
    private final static Date deptTime = new Date();
    @InjectMocks
    private FlightSearchServiceImpl flightSearchService;

    @BeforeEach
    public void setUp() {
        flightSearchService = new FlightSearchServiceImpl(new FlightAvailabilityService() {
            @Override
            public Collection<FlightSummary> getAvailableFlights(FlightAvailabilityRequest request) {
                return getFlightSummariesAfterEachMinutesProvided(10L, 10, 1000, false);
            }
        });
    }

    @Test
    void fetchFlightsBasedOnRequestDefault() {

        FlightSearchRequest request = new FlightSearchRequest("DOHA", "CPH",
                String.valueOf(Instant.now().toEpochMilli()));

        Collection<FlightSearchDto> searchDtos = flightSearchService.fetchFlightsBasedOnRequest(request);
        Assertions.assertEquals(searchDtos.size(), 3);
    }

    private Collection<FlightSummary> getFlightSummariesAfterEachMinutesProvided(
            Long min, int noOfRecords, float avgPrice, Boolean isCancellationPossible) {
        List<FlightSummary> summaryCollections = new ArrayList<>();
        for (int i = 0; i < noOfRecords; i++) {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(min, ChronoUnit.MINUTES));
            Date arrTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            FlightSummary summary = new FlightSummary(airlineCode, deptTime, arrTime, avgPrice, isCancellationPossible);
            summaryCollections.add(summary);
            min = min + 10;
        }
        return summaryCollections;
    }
}
