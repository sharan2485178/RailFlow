package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.api.APIResponse;
import com.example.dto.LocomotiveAssignmentRequest;
import com.example.dto.LocomotiveAssignmentResponse;
import com.example.service.LocomotiveAssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/locomotive-assignments")
public class LocomotiveAssignmentController {

    private final LocomotiveAssignmentService locomotiveAssignmentService;

    public LocomotiveAssignmentController(
            LocomotiveAssignmentService locomotiveAssignmentService) {
        this.locomotiveAssignmentService = locomotiveAssignmentService;
    }

    // POST /api/locomotive-assignments
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<LocomotiveAssignmentResponse>> assign(
            @Valid @RequestBody LocomotiveAssignmentRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(
                        "Locomotive assigned successfully",
                        locomotiveAssignmentService.assign(req)));
    }

    // GET /api/locomotive-assignments
    
}