package com.example.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.AssignSlotRequest;
import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.dto.YardSlotResponse;
import com.example.service.ShuntingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shunting")
public class ShuntingController {

    private final ShuntingService shuntingService;

    public ShuntingController(ShuntingService shuntingService) {
        this.shuntingService = shuntingService;
    }

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<APIResponse<ShuntingResponse>> shunt(
            @Valid @RequestBody ShuntingRequest req,
            Authentication auth) {
        ShuntingResponse response = shuntingService.shunt(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Shunting operation completed successfully", response));
    }

    /**
     * PUT /api/shunting/yards/{yardId}/slots/{slotId}/assign
     * Assign a wagon or locomotive to a specific yard slot.
     */
    @PutMapping("/yards/{yardId}/slots/{slotId}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<APIResponse<YardSlotResponse>> assignSlot(
            @PathVariable Long yardId,
            @PathVariable Long slotId,
            @Valid @RequestBody AssignSlotRequest req,
            Authentication auth) {
        YardSlotResponse response = shuntingService.assignSlot(yardId, slotId, req, auth.getName());
        return ResponseEntity.ok(
                APIResponse.success("Slot assigned successfully", response));
    }

    /**
     * PUT /api/shunting/yards/{yardId}/slots/{slotId}/release
     * Release the asset currently occupying a yard slot.
     */
    @PutMapping("/yards/{yardId}/slots/{slotId}/release")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<APIResponse<YardSlotResponse>> releaseSlot(
            @PathVariable Long yardId,
            @PathVariable Long slotId,
            Authentication auth) {
        YardSlotResponse response = shuntingService.releaseSlot(yardId, slotId, auth.getName());
        return ResponseEntity.ok(
                APIResponse.success("Slot released successfully", response));
    }

    /**
     * GET /api/shunting/yards/{yardId}/slots
     * View the slot map for a specific yard.
     */
    @GetMapping("/yards/{yardId}/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<APIResponse<List<YardSlotResponse>>> getSlotsByYard(
            @PathVariable Long yardId) {
        return ResponseEntity.ok(
                APIResponse.success("Slots fetched successfully",
                        shuntingService.getSlotsByYard(yardId)));
    }
}