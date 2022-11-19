package com.qatarairways.adapter.flight.dto.request;

import com.qatarairways.adapter.flight.enums.FlightStatus;
import lombok.*;

@Data
@Builder(builderClassName="Builder")
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest{
    @NonNull
    private String origin;
    @NonNull
    private String destination;
    @NonNull
    private String departureDate;
    private int numberOfTravellers;
    private FlightStatus flightStatus;
    private Long maxPriceInUsd;
    public static class Builder{
        //Set defaults here
        private int numberOfTravellers = 1;
        private FlightStatus flightStatus = FlightStatus.NA;
    }
}
