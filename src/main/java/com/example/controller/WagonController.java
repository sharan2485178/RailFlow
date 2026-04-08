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
import com.example.dto.WagonRequest;
import com.example.dto.WagonResponse;
import com.example.dto.WagonStatusRequest;
import com.example.service.WagonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wagons")
public class WagonController {

    @Autowired private WagonService wagonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WagonResponse>> register(
            @Valid @RequestBody WagonRequest req, Authentication auth) {

        WagonResponse response = wagonService.register(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Wagon registered successfully.", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<List<WagonResponse>>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {

        List<WagonResponse> response = wagonService.getAll(type, status);
        return ResponseEntity.ok(APIResponse.success("Wagons fetched successfully.", response));
    }

    @GetMapping("/{wagonId}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<WagonResponse>> getById(@PathVariable Long wagonId) {

        WagonResponse response = wagonService.getById(wagonId);
        return ResponseEntity.ok(APIResponse.success("Wagon fetched successfully.", response));
    }

    @PutMapping("/{wagonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WagonResponse>> update(
            @PathVariable Long wagonId,
            @Valid @RequestBody WagonRequest req,
            Authentication auth) {

        WagonResponse response = wagonService.update(wagonId, req, auth.getName());
        return ResponseEntity.ok(APIResponse.success("Wagon updated successfully.", response));
    }

    @PutMapping("/{wagonId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE')")
    public ResponseEntity<APIResponse<WagonResponse>> changeStatus(
            @PathVariable Long wagonId,
            @Valid @RequestBody WagonStatusRequest req,
            Authentication auth) {

        WagonResponse response = wagonService.changeStatus(wagonId, req, auth.getName());
        return ResponseEntity.ok(APIResponse.success("Wagon status updated successfully.", response));
    }
}