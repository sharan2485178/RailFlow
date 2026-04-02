package com.example.controller;

import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.dto.MaintenanceStatusRequest;
import com.example.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    @Autowired private MaintenanceRecordService maintenanceRecordService;

    /**
     * POST /api/maintenance-records
     * Create a new maintenance log for an asset.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER')")
    public ResponseEntity<MaintenanceResponse> create(@Valid @RequestBody MaintenanceRequest req,
                                                      Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(maintenanceRecordService.create(req, auth.getName()));
    }

    /**
     * GET /api/maintenance-records
     * List all maintenance records.
     * Optional filters: ?status=OPEN|IN_PROGRESS|COMPLETED|CANCELLED
     *                   ?timetableId={id}
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<List<MaintenanceResponse>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long timetableId) {
        return ResponseEntity.ok(maintenanceRecordService.getAll(status, timetableId));
    }

    /**
     * GET /api/maintenance-records/{id}
     * Retrieve a specific maintenance record by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<MaintenanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceRecordService.getById(id));
    }

    /**
     * PUT /api/maintenance-records/{id}/status
     * Update the status of a maintenance record (e.g. OPEN → IN_PROGRESS → COMPLETED).
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER')")
    public ResponseEntity<MaintenanceResponse> changeStatus(@PathVariable Long id,
                                                            @Valid @RequestBody MaintenanceStatusRequest req,
                                                            Authentication auth) {
        return ResponseEntity.ok(maintenanceRecordService.changeStatus(id, req, auth.getName()));
    }
}
