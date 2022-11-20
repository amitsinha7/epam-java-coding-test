package com.qatarairways.adapter.flight.services;

import com.qatarairways.adapter.flight.dto.request.FlightSearchRequest;
import com.qatarairways.adapter.flight.dto.response.FlightSearchResponse;
import com.qatarairways.adapter.flight.enums.SortBy;
import com.qatarairways.adapter.flight.services.impl.FlightSearchServiceImpl;
import com.qatarairways.adapter.flight.views.FlightSummary;
import org.junit.jupiter.api.Assertions;
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
                String.valueOf(Instant.now().toEpochMilli()),1,null, SortBy.DURATION,
                0L,0,3, SortOrder.ASCENDING);

        Collection<FlightSearchResponse> searchDtos = flightSearchService.fetchFlightsBasedOnRequest(request);
        Assertions.assertEquals(searchDtos.size(), request.getSize());
    }

    private Collection<FlightSummary> getFlightSummariesRandom() {
        List<FlightSummary> summaryCollections = new ArrayList<>();
        long min = 10;
        float avgPrice = 1000;
        boolean isCancellationPossible = false;
        for (int i = 0; i < 100; i++) {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(min, ChronoUnit.MINUTES));
            Date arrTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            FlightSummary summary = new FlightSummary(airlineCode, deptTime, arrTime, avgPrice, isCancellationPossible);
            summaryCollections.add(summary);
            min = min + 10;
            avgPrice = avgPrice+5;
            if(isCancellationPossible){
                isCancellationPossible = false;
                avgPrice =avgPrice -6;
                min = min-11;
            } else {
                isCancellationPossible = true;
            }
        }
        return summaryCollections;
    }
}
