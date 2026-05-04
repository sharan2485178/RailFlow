package com.example.exception;

import java.time.LocalDateTime;

public class InvalidTimeRangeException extends RuntimeException {

    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;

    public InvalidTimeRangeException(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        super("Departure time " + departureTime + " must be before arrival time " + arrivalTime);
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
}