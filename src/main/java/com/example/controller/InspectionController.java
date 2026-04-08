package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;
import com.example.service.InspectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /**
     * POST /api/inspections
     * Create a new inspection record.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','MAINTENANCE')")
    public ResponseEntity<APIResponse<InspectionResponse>> create(
            @Valid @RequestBody InspectionRequest req,
            Authentication auth) {
        InspectionResponse response = inspectionService.create(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Inspection record created successfully", response));
    }

    /**
     * GET /api/inspections
     * Retrieve all inspection records.
     * Optional filters: ?result=PENDING|PASS|FAIL  ?timetableId=1
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','MAINTENANCE','DISPATCHER','AUDITOR')")
    public ResponseEntity<APIResponse<List<InspectionResponse>>> getAll(
            @RequestParam(required = false) String result,
            @RequestParam(required = false) Long timetableId) {
        return ResponseEntity.ok(
                APIResponse.success("Inspection records fetched successfully",
                        inspectionService.getAll(result, timetableId)));
    }

    /**
     * GET /api/inspections/{id}
     * Retrieve a single inspection record by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','MAINTENANCE','DISPATCHER','AUDITOR')")
    public ResponseEntity<APIResponse<InspectionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Inspection record fetched successfully",
                        inspectionService.getById(id)));
    }
}