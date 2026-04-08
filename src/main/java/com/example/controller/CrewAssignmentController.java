package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.CrewAssignmentRequest;
import com.example.dto.CrewAssignmentResponse;
import com.example.dto.UserResponse;
import com.example.service.CrewAssignmentService;
import com.example.service.UserService;

import jakarta.validation.Valid;

@RestController
public class CrewAssignmentController {

    private final CrewAssignmentService crewAssignmentService;
    private final UserService userService;

    public CrewAssignmentController(CrewAssignmentService crewAssignmentService,
                          UserService userService) {
        this.crewAssignmentService = crewAssignmentService;
        this.userService = userService;
    }

    // POST /api/crew — register crew member
   

    // GET /api/crew — list all available crew
    @GetMapping("/api/crew")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<List<UserResponse>>> getAvailableCrew() {

        return ResponseEntity.ok(
                APIResponse.success("Crew fetched successfully",
                        crewAssignmentService.getAvailableCrew()));
    }

    // POST /api/crew-assignments — assign crew to timetable
    @PostMapping("/api/crew-assignments")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<CrewAssignmentResponse>> assign(
            @Valid @RequestBody CrewAssignmentRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Crew assigned successfully",
                        crewAssignmentService.assign(req)));
    }

    // PUT /api/crew-assignments/{id}/confirm
    // only the assigned engineer confirms their own assignment
    @PutMapping("/api/crew-assignments/{id}/confirm")
    @PreAuthorize("hasAnyRole('LOCOMOTIVE_ENGINEER','MAINTENANCE_CREW','YARD_MANAGER')")
    public ResponseEntity<APIResponse<CrewAssignmentResponse>> confirm(
            @PathVariable Long id,
            Authentication auth) {

        
        return ResponseEntity.ok(
                APIResponse.success("Assignment confirmed successfully",
                        crewAssignmentService.confirm(id, auth.getName())));
    }
}