package com.example.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResolveConflictRequest {

    @NotBlank(message = "Resolution note is required")
    private String resolutionNote;

    // new times for the ON_HOLD timetable (timetable1)
    @NotNull(message = "New departure time is required")
    private LocalDateTime newDepartureTime;

    @NotNull(message = "New arrival time is required")
    private LocalDateTime newArrivalTime;
}