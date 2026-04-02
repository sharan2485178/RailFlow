package com.example.dto;

import com.example.model.AssetType;
import jakarta.validation.constraints.NotNull;

public class AssignSlotRequest {

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    // ── Getters & Setters ────────────────────────────────────────

    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
}
