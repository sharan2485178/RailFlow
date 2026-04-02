package com.example.dto;

import com.example.model.AssetType;
import jakarta.validation.constraints.NotNull;

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

    public Long getFromSlotId() { return fromSlotId; }
    public void setFromSlotId(Long fromSlotId) { this.fromSlotId = fromSlotId; }

    public Long getToSlotId() { return toSlotId; }
    public void setToSlotId(Long toSlotId) { this.toSlotId = toSlotId; }

    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
