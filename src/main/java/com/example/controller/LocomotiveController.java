package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveResponse;
import com.example.dto.LocomotiveStatusRequest;
import com.example.service.LocomotiveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/locomotive")
public class LocomotiveController {

    @Autowired private LocomotiveService locomotiveService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<LocomotiveResponse>> register(
            @Valid @RequestBody LocomotiveRequest req, Authentication auth) {

        LocomotiveResponse response = locomotiveService.register(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Locomotive registered successfully.", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<List<LocomotiveResponse>>> getAll(
            @RequestParam(required = false) String status) {

        List<LocomotiveResponse> response = locomotiveService.getAll(status);
        return ResponseEntity.ok(APIResponse.success("Locomotives fetched successfully.", response));
    }

    @PutMapping("/{locoId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE')")
    public ResponseEntity<APIResponse<LocomotiveResponse>> changeStatus(
            @PathVariable Long locoId,
            @Valid @RequestBody LocomotiveStatusRequest req,
            Authentication auth) {

        LocomotiveResponse response = locomotiveService.changeStatus(locoId, req, auth.getName());
        return ResponseEntity.ok(APIResponse.success("Locomotive status updated successfully.", response));
    }
}