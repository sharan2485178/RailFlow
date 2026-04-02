package com.example.controller;

import com.example.dto.ManifestRequest;
import com.example.dto.ManifestStatusRequest;
import com.example.model.Manifest;
import com.example.service.ManifestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manifests")
public class ManifestController {

    @Autowired private ManifestService manifestService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<Manifest> create(@Valid @RequestBody ManifestRequest req,
                                           Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(manifestService.create(req, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<List<Manifest>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(manifestService.getAll(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<Manifest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(manifestService.getById(id));
    }

    @GetMapping("/train/{trainId}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR')")
    public ResponseEntity<List<Manifest>> getByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(manifestService.getByTrain(trainId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<Manifest> update(@PathVariable Long id,
                                           @Valid @RequestBody ManifestRequest req,
                                           Authentication auth) {
        return ResponseEntity.ok(manifestService.update(id, req, auth.getName()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<Manifest> changeStatus(@PathVariable Long id,
                                                 @Valid @RequestBody ManifestStatusRequest req,
                                                 Authentication auth) {
        return ResponseEntity.ok(manifestService.changeStatus(id, req, auth.getName()));
    }
}
