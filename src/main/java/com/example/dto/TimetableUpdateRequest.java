package com.example.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TimetableUpdateRequest {

    @NotNull
    @Future(message="Departure Time must be in the future")
    private LocalDateTime departureTime;

    @NotNull
    @Future(message="Arrival Time must be in the future")
    private LocalDateTime arrivalTime;

    @NotBlank(message="PathCode is required")
    private String pathCode;

    







    
}
