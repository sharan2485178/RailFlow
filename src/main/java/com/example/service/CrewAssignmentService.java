package com.example.service;

import java.util.List;

import com.example.dto.CrewAssignmentRequest;
import com.example.dto.CrewAssignmentResponse;
import com.example.dto.UserResponse;

public interface CrewAssignmentService {
    CrewAssignmentResponse assign(CrewAssignmentRequest req);
    List<CrewAssignmentResponse> getByTimetable(Long timetableId);
    CrewAssignmentResponse confirm(Long assignmentId, String email);
    List<UserResponse> getAvailableCrew();
}
