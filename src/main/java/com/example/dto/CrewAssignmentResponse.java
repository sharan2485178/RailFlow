package com.example.dto;

import com.example.enums.CrewAssignmentStatus;
import com.example.enums.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewAssignmentResponse {

    private Long id;
    private Long timetableId;
    private Long userId;
    private String userName;
    private Role crewRole;
    private CrewAssignmentStatus status;
	

    
}