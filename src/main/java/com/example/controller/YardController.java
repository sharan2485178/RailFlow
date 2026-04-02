package com.example.controller;

import com.example.dto.AssignSlotRequest;
import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.dto.YardRequest;
import com.example.dto.YardSlotRequest;
import com.example.model.Yard;
import com.example.model.YardSlot;
import com.example.service.ShuntingService;
import com.example.service.YardSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/yards")
public class YardController {

    @Autowired private YardSlotService yardSlotService;
    @Autowired private ShuntingService shuntingService;
    @Autowired private com.example.repository.YardRepository yardRepository;

    // ── Yard CRUD ─────────────────────────────────────────────────

    /**
     * POST /api/yards
     * Register a new yard.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<Yard> createYard(@Valid @RequestBody YardRequest req,
                                           Authentication auth) {
        if (yardRepository.existsByName(req.getName())) {
            throw new RuntimeException("Yard with name '" + req.getName() + "' already exists");
        }
        Yard yard = new Yard();
        yard.setName(req.getName());
        yard.setLocation(req.getLocation());
        yard.setTotalSlots(req.getTotalSlots());
        yard.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(yardRepository.save(yard));
    }

    /**
     * GET /api/yards
     * List all yards.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<List<Yard>> getAllYards() {
        return ResponseEntity.ok(yardRepository.findAll());
    }

    // ── Slot Management ───────────────────────────────────────────

    /**
     * POST /api/yards/slots
     * Configure (create) a new slot — existing endpoint kept for backward compatibility.
     */
    @PostMapping("/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<YardSlot> configureSlot(@Valid @RequestBody YardSlotRequest req,
                                                   Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(yardSlotService.configure(req, auth.getName()));
    }

    /**
     * GET /api/yards/slots
     * View all slots across all yards (global slot map).
     */
    @GetMapping("/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<List<YardSlot>> getSlotMap() {
        return ResponseEntity.ok(yardSlotService.getSlotMap());
    }

    /**
     * GET /api/yards/{yardId}/slots
     * View the slot map for a specific yard — track available space.
     */
    @GetMapping("/{yardId}/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<List<YardSlot>> getSlotsByYard(@PathVariable Long yardId) {
        return ResponseEntity.ok(shuntingService.getSlotsByYard(yardId));
    }

    // ── Assign / Release ──────────────────────────────────────────

    /**
     * PUT /api/yards/{yardId}/slots/{slotId}/assign
     * Assign a wagon or locomotive to a specific yard slot.
     */
    @PutMapping("/{yardId}/slots/{slotId}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<YardSlot> assignSlot(@PathVariable Long yardId,
                                               @PathVariable Long slotId,
                                               @Valid @RequestBody AssignSlotRequest req,
                                               Authentication auth) {
        return ResponseEntity.ok(shuntingService.assignSlot(yardId, slotId, req, auth.getName()));
    }

    /**
     * PUT /api/yards/{yardId}/slots/{slotId}/release
     * Release (clear) the asset currently occupying a yard slot.
     */
    @PutMapping("/{yardId}/slots/{slotId}/release")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<YardSlot> releaseSlot(@PathVariable Long yardId,
                                                @PathVariable Long slotId,
                                                Authentication auth) {
        return ResponseEntity.ok(shuntingService.releaseSlot(yardId, slotId, auth.getName()));
    }

    // ── Shunting ──────────────────────────────────────────────────

    /**
     * POST /api/yards/shunting
     * Move an asset (wagon/locomotive) from one slot to another within the yard.
     */
    @PostMapping("/shunting")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<ShuntingResponse> shunt(@Valid @RequestBody ShuntingRequest req,
                                                  Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shuntingService.shunt(req, auth.getName()));
    }
}
