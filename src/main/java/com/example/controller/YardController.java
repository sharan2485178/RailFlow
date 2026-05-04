package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.YardRequest;
import com.example.dto.YardResponse;
import com.example.service.YardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/yards")
public class YardController {

    private final YardService yardService;

    public YardController(YardService yardService) {
        this.yardService = yardService;
    }

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<APIResponse<YardResponse>> createYard(
            @Valid @RequestBody YardRequest req,
            Authentication auth) {
        YardResponse response = yardService.create(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Yard created successfully", response));
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<APIResponse<List<YardResponse>>> getAllYards() {
        return ResponseEntity.ok(
                APIResponse.success("Yards fetched successfully", yardService.getAll()));
    }
}