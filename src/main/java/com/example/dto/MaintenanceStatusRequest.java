package com.example.dto;

import com.example.enums.MaintenanceStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class MaintenanceStatusRequest {

    @NotNull
    private MaintenanceStatus status;

    private String notes;

    // ── Getters & Setters ────────────────────────────────────────

    
}
