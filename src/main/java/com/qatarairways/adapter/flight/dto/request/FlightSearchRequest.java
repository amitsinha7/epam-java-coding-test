package com.qatarairways.adapter.flight.dto.request;

import com.qatarairways.adapter.flight.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.swing.*;

@Data
@AllArgsConstructor
public class FlightSearchRequest {
    @NonNull
    private String origin;
    @NonNull
    private String destination;
    @NonNull
    private String departureDateTime;
    private int numberOfTravellers;
    private Boolean isCancellationPossible;
    private SortBy sortBy;
    private Long maxPrice;
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 3;
    private SortOrder order;
}
