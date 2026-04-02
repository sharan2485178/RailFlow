package com.example.controller;

import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;
import com.example.service.InspectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    @Autowired private InspectionService inspectionService;

    /**
     * POST /api/inspections
     * Create a new inspection record for a wagon, locomotive, or train.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER')")
    public ResponseEntity<InspectionResponse> create(@Valid @RequestBody InspectionRequest req,
                                                     Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inspectionService.create(req, auth.getName()));
    }

    /**
     * GET /api/inspections
     * Retrieve all inspection records.
     * Optional filters: ?result=PASS|FAIL|PENDING  or  ?timetableId={id}
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<List<InspectionResponse>> getAll(
            @RequestParam(required = false) String result,
            @RequestParam(required = false) Long timetableId) {
        return ResponseEntity.ok(inspectionService.getAll(result, timetableId));
    }

    /**
     * GET /api/inspections/{id}
     * Retrieve a single inspection record by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<InspectionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inspectionService.getById(id));
    }
}
