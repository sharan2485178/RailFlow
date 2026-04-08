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
import com.example.dto.TrainRequest;
import com.example.dto.TrainResponse;
import com.example.dto.TrainStatusRequest;
import com.example.service.TrainService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired private TrainService trainService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<TrainResponse>> register(
            @Valid @RequestBody TrainRequest req, Authentication auth) {

        TrainResponse response = trainService.register(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Train registered successfully.", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<List<TrainResponse>>> getAll(
            @RequestParam(required = false) String status) {

        List<TrainResponse> response = trainService.getAll(status);
        return ResponseEntity.ok(APIResponse.success("Trains fetched successfully.", response));
    }

    @PutMapping("/{trainId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<TrainResponse>> changeStatus(
            @PathVariable Long trainId,
            @Valid @RequestBody TrainStatusRequest req,
            Authentication auth) {

        TrainResponse response = trainService.changeStatus(trainId, req, auth.getName());
        return ResponseEntity.ok(APIResponse.success("Train status updated successfully.", response));
    }
}