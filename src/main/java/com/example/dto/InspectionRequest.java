package com.example.dto;

import java.time.LocalDate;

import com.example.enums.AssetType;
import com.example.enums.InspectionStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class InspectionRequest {

    private Long timetableId;

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    @NotNull
    private LocalDate inspectionDate;

    @NotBlank
    private String inspectedBy;

    @NotNull
    private InspectionStatus result;

    private String remarks;

    // ── Getters & Setters ────────────────────────────────────────

    
}
