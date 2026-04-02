package com.example.controller;

import com.example.dto.TrainRequest;
import com.example.dto.TrainStatusRequest;
import com.example.model.Train;
import com.example.service.TrainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired private TrainService trainService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Train> register(@Valid @RequestBody TrainRequest req,
                                          Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(trainService.register(req, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<List<Train>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(trainService.getAll(status));
    }

    @PutMapping("/{trainId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Train> changeStatus(@PathVariable Long trainId,
                                              @Valid @RequestBody TrainStatusRequest req,
                                              Authentication auth) {
        return ResponseEntity.ok(trainService.changeStatus(trainId, req, auth.getName()));
    }
}
