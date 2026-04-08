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
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.AddBookingToManifestRequest;
import com.example.dto.ManifestBookingResponse;
import com.example.dto.ManifestRequest;
import com.example.dto.ManifestResponse;
import com.example.service.ManifestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/manifests")
public class ManifestController {

    private final ManifestService manifestService;

    public ManifestController(ManifestService manifestService) {
        this.manifestService = manifestService;
    }

    // POST /api/manifests
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<ManifestResponse>> create(
            @Valid @RequestBody ManifestRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Manifest created successfully",
                        manifestService.create(req, auth.getName())));
    }

    // GET /api/manifests
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','AUDITOR','BILLING')")
    public ResponseEntity<APIResponse<List<ManifestResponse>>> getAll() {
        return ResponseEntity.ok(
                APIResponse.success("Manifests fetched successfully",
                        manifestService.getAll()));
    }

    // GET /api/manifests/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','AUDITOR','BILLING','FREIGHT_FORWARDER')")
    public ResponseEntity<APIResponse<ManifestResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Manifest fetched successfully",
                        manifestService.getById(id)));
    }

    // POST /api/manifests/{id}/bookings
    @PostMapping("/{id}/bookings")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<ManifestBookingResponse>> addBooking(
            @PathVariable Long id,
            @Valid @RequestBody AddBookingToManifestRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Booking added to manifest successfully",
                        manifestService.addBooking(id, req)));
    }

    // GET /api/manifests/{id}/bookings
    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','AUDITOR','FREIGHT_FORWARDER')")
    public ResponseEntity<APIResponse<List<ManifestBookingResponse>>> getBookings(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Manifest bookings fetched successfully",
                        manifestService.getBookings(id)));
    }
}