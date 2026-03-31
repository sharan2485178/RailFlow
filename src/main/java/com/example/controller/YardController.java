package com.example.controller;

import com.example.dto.YardSlotRequest;
import com.example.model.YardSlot;
import com.example.service.YardSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yards")
public class YardController {

    @Autowired private YardSlotService yardSlotService;

    @PostMapping("/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER')")
    public ResponseEntity<YardSlot> configureSlot(@Valid @RequestBody YardSlotRequest req,
                                                   Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(yardSlotService.configure(req, auth.getName()));
    }

    @GetMapping("/slots")
    @PreAuthorize("hasAnyRole('ADMIN','YARD_MANAGER','DISPATCHER')")
    public ResponseEntity<List<YardSlot>> getSlotMap() {
        return ResponseEntity.ok(yardSlotService.getSlotMap());
    }
}
