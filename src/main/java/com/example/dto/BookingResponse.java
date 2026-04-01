package com.example.dto;

import com.example.model.Booking;
import com.example.model.BookingStatus;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long userId;
    private String origin;
    private String destination;
    private String cargoDetailsJson;
    private Double weightTon;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public static BookingResponse fromBooking(Booking b) {
        BookingResponse r = new BookingResponse();
        r.id = b.getId();
        r.userId = b.getUser().getId();
        r.origin = b.getOrigin();
        r.destination = b.getDestination();
        r.cargoDetailsJson = b.getCargoDetailsJson();
        r.weightTon = b.getWeightTon();
        r.status = b.getStatus();
        r.createdAt = b.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getCargoDetailsJson() { return cargoDetailsJson; }
    public Double getWeightTon() { return weightTon; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
