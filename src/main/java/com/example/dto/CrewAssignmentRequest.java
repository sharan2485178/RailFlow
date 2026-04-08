package com.example.dto;

import com.example.enums.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CrewAssignmentRequest {

    @NotNull(message = "Timetable ID is required")
    private Long timetableId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Crew role is required")
    private Role crewRole;

    
}