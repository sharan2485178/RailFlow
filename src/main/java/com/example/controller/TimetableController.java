package com.example.controller;

import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.model.Timetable;
import com.example.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
public class TimetableController {

    @Autowired private TimetableService timetableService;

    /**
     * POST /api/timetables
     * Create a new train timetable with assigned wagons and locomotives.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<Timetable> create(@Valid @RequestBody TimetableRequest req,
                                            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timetableService.create(req, auth.getName()));
    }

    /**
     * GET /api/timetables
     * Retrieve all timetables, optionally filtered by ?status=SCHEDULED|IN_PROGRESS|COMPLETED|CANCELLED
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','ENGINEER','MAINTENANCE')")
    public ResponseEntity<List<Timetable>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(timetableService.getAll(status));
    }

    /**
     * GET /api/timetables/{id}
     * Retrieve a single timetable by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','ENGINEER','MAINTENANCE')")
    public ResponseEntity<Timetable> getById(@PathVariable Long id) {
        return ResponseEntity.ok(timetableService.getById(id));
    }

    /**
     * GET /api/timetables/{id}/assets
     * Retrieve all wagons and locomotives assigned to a specific timetable.
     * Used by the Inspection and Maintenance modules to list available assets.
     */
    @GetMapping("/{id}/assets")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','ENGINEER','MAINTENANCE','YARD_MANAGER')")
    public ResponseEntity<TimetableAssetsResponse> getAssets(@PathVariable Long id) {
        return ResponseEntity.ok(timetableService.getAssets(id));
    }
}
