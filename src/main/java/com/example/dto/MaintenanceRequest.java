package com.example.dto;

import java.time.LocalDate;

import com.example.enums.AssetType;
import com.example.enums.MaintenancePriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class MaintenanceRequest {

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    @NotBlank
    private String description;

    @NotNull
    private MaintenancePriority priority;

    private Long timetableId;
    private String assignedTo;
    private LocalDate scheduledDate;
    private String notes;

    // ── Getters & Setters ────────────────────────────────────────

    
}
