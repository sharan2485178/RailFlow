package com.example.controller;

import com.example.dto.WagonRequest;
import com.example.dto.WagonStatusRequest;
import com.example.model.Wagon;
import com.example.service.WagonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wagons")
public class WagonController {

    @Autowired private WagonService wagonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Wagon> register(@Valid @RequestBody WagonRequest req,
                                          Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wagonService.register(req, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','YARD_MANAGER')")
    public ResponseEntity<List<Wagon>> getAll(@RequestParam(required = false) String type,
                                              @RequestParam(required = false) String status) {
        return ResponseEntity.ok(wagonService.getAll(type, status));
    }

    @PutMapping("/{wagonId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MAINTENANCE')")
    public ResponseEntity<Wagon> changeStatus(@PathVariable Long wagonId,
                                              @Valid @RequestBody WagonStatusRequest req,
                                              Authentication auth) {
        return ResponseEntity.ok(wagonService.changeStatus(wagonId, req, auth.getName()));
    }
}
