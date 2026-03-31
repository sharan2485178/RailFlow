package com.example.controller;

import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveStatusRequest;
import com.example.model.Locomotive;
import com.example.service.LocomotiveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locomotive")
public class LocomotiveController {

    @Autowired private LocomotiveService locomotiveService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Locomotive> register(@Valid @RequestBody LocomotiveRequest req,
                                               Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(locomotiveService.register(req, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<List<Locomotive>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(locomotiveService.getAll(status));
    }

    @PutMapping("/{locoId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE')")
    public ResponseEntity<Locomotive> changeStatus(@PathVariable Long locoId,
                                                   @Valid @RequestBody LocomotiveStatusRequest req,
                                                   Authentication auth) {
        return ResponseEntity.ok(locomotiveService.changeStatus(locoId, req, auth.getName()));
    }
}
