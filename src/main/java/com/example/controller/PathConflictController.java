package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.PathConflictResponse;
import com.example.dto.ResolveConflictRequest;
import com.example.service.PathConflictService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/path-conflicts")
public class PathConflictController {

    private final PathConflictService pathConflictService;

    public PathConflictController(PathConflictService pathConflictService) {
        this.pathConflictService = pathConflictService;
    }

    /**
     * GET /api/path-conflicts
     * Retrieve all unresolved path conflicts.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','YARD_MANAGER')")
    public ResponseEntity<APIResponse<List<PathConflictResponse>>> getAllUnresolved() {
        return ResponseEntity.ok(
                APIResponse.success("Unresolved path conflicts fetched successfully",
                        pathConflictService.getAllUnresolved()));
    }
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<PathConflictResponse>> resolve(
            @PathVariable Long id,
            @Valid @RequestBody ResolveConflictRequest req,
            Authentication auth) {
        return ResponseEntity.ok(
                APIResponse.success("Conflict resolved successfully",
                        pathConflictService.resolve(id, req, auth.getName())));
    }
}