package com.example.controller;

import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.dto.BookingStatusRequest;
import com.example.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR','USER')")
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest req,
                                                  Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookingService.create(req, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR')")
    public ResponseEntity<List<BookingResponse>> getAll(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(bookingService.getAll(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<List<BookingResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getByUser(userId));
    }

    @GetMapping("/confirmed")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<List<BookingResponse>> getConfirmed() {
        return ResponseEntity.ok(bookingService.getAll("CONFIRMED"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<BookingResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody BookingRequest req,
                                                  Authentication auth) {
        return ResponseEntity.ok(bookingService.update(id, req, auth.getName()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<BookingResponse> changeStatus(@PathVariable Long id,
                                                        @Valid @RequestBody BookingStatusRequest req,
                                                        Authentication auth) {
        return ResponseEntity.ok(bookingService.changeStatus(id, req, auth.getName()));
    }
}
