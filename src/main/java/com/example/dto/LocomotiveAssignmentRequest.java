package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class LocomotiveAssignmentRequest {

    @NotNull(message = "Timetable ID is required")
    private Long timetableId;

    @NotNull(message = "Locomotive ID is required")
    private Long locomotiveId;

    
}