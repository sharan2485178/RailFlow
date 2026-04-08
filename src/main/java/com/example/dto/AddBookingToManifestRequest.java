package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddBookingToManifestRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull
    private Long manifestId;
    
}