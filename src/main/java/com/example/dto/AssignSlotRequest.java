package com.example.dto;

import com.example.enums.AssetType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AssignSlotRequest {

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    // ── Getters & Setters ────────────────────────────────────────

    
}
