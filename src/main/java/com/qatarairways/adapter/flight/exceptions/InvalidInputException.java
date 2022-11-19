package com.qatarairways.adapter.flight.exceptions;

import java.io.Serializable;

public class InvalidInputException extends RuntimeException implements Serializable {

    public InvalidInputException(String message) {
        super(message);
    }
}
