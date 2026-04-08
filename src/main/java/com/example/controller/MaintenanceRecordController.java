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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.dto.MaintenanceStatusRequest;
import com.example.service.MaintenanceRecordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    /**
     * POST /api/maintenance-records
     * Create a new maintenance log for an asset.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER')")
    public ResponseEntity<APIResponse<MaintenanceResponse>> create(
            @Valid @RequestBody MaintenanceRequest req,
            Authentication auth) {
        MaintenanceResponse response = maintenanceRecordService.create(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Maintenance record created successfully", response));
    }

    /**
     * GET /api/maintenance-records
     * List all maintenance records.
     * Optional filters: ?status=OPEN|IN_PROGRESS|COMPLETED|CANCELLED  ?timetableId={id}
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<APIResponse<List<MaintenanceResponse>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long timetableId) {
        return ResponseEntity.ok(
                APIResponse.success("Maintenance records fetched successfully",
                        maintenanceRecordService.getAll(status, timetableId)));
    }

    /**
     * GET /api/maintenance-records/{id}
     * Retrieve a specific maintenance record by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER','DISPATCHER')")
    public ResponseEntity<APIResponse<MaintenanceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Maintenance record fetched successfully",
                        maintenanceRecordService.getById(id)));
    }

    /**
     * PUT /api/maintenance-records/{id}/status
     * Update the status of a maintenance record (OPEN → IN_PROGRESS → COMPLETED).
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE','ENGINEER')")
    public ResponseEntity<APIResponse<MaintenanceResponse>> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Maintenance record status updated successfully",
                        maintenanceRecordService.changeStatus(id, req, auth.getName())));
    }
}