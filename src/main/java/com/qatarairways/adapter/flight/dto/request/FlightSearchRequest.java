package com.qatarairways.adapter.flight.dto.request;

import com.qatarairways.adapter.flight.enums.FlightSort;
import com.qatarairways.adapter.flight.enums.FlightStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSearchRequest {
    @NonNull
    private String origin;
    @NonNull
    private String destination;
    @NonNull
    private String departureDateTime;
    @Builder.Default
    private int numberOfTravellers = 1;
    @Builder.Default
    private FlightStatus flightStatus = FlightStatus.NA;
    @Builder.Default
    private Long maxPriceInUsd = 0L;
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 3;
    @Builder.Default
    private FlightSort flightSort = FlightSort.DURATION;
    @Builder.Default
    private String order = "ASC";

    public FlightSearchRequest(@NonNull String origin, @NonNull String destination, @NonNull String departureDateTime) {
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.numberOfTravellers = 1;
        this.flightStatus = FlightStatus.NA;
        this.page = 0;
        this.size = 3;
        this.flightSort = FlightSort.DURATION;
        this.order = "ASC";
        this.maxPriceInUsd = 0L;
    }
}
