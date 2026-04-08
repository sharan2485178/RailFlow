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
import com.example.dto.YardSlotRequest;
import com.example.dto.YardSlotResponse;
import com.example.service.YardSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/yard-slots")
public class YardSlotController {

    private final YardSlotService yardSlotService;

    public YardSlotController(YardSlotService yardSlotService) {
        this.yardSlotService = yardSlotService;
    }

    /**
     * POST /api/yard-slots
     * Configure (create) a new yard slot.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<APIResponse<YardSlotResponse>> configureSlot(
            @Valid @RequestBody YardSlotRequest req,
            Authentication auth) {
        YardSlotResponse response = yardSlotService.configure(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Slot configured successfully", response));
    }

    /**
     * GET /api/yard-slots
     * Retrieve all slots across all yards (global slot map).
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<APIResponse<List<YardSlotResponse>>> getSlotMap() {
        return ResponseEntity.ok(
                APIResponse.success("Slot map fetched successfully", yardSlotService.getSlotMap()));
    }

    /**
     * GET /api/yard-slots/yard/{yardId}
     * Retrieve all slots for a specific yard.
     */
    @GetMapping("/yard/{yardId}")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<APIResponse<List<YardSlotResponse>>> getSlotsByYard(
            @PathVariable Long yardId) {
        return ResponseEntity.ok(
                APIResponse.success("Slots fetched successfully", yardSlotService.getByYard(yardId)));
    }
}