package com.example.dto;

import com.example.enums.AssetType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ShuntingRequest {

    @NotNull
    private Long fromSlotId;

    @NotNull
    private Long toSlotId;

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    private String notes;

    // ── Getters & Setters ────────────────────────────────────────

    
}
