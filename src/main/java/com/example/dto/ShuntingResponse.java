package com.example.dto;

import com.example.model.AssetType;
import com.example.model.ShuntingOperation;
import java.time.LocalDateTime;

public class ShuntingResponse {

    private Long id;
    private Long fromSlotId;
    private Long toSlotId;
    private AssetType assetType;
    private Long assetId;
    private String performedBy;
    private LocalDateTime performedAt;
    private String notes;

    public static ShuntingResponse fromOperation(ShuntingOperation op) {
        ShuntingResponse res = new ShuntingResponse();
        res.id = op.getId();
        res.fromSlotId = op.getFromSlotId();
        res.toSlotId = op.getToSlotId();
        res.assetType = op.getAssetType();
        res.assetId = op.getAssetId();
        res.performedBy = op.getPerformedBy();
        res.performedAt = op.getPerformedAt();
        res.notes = op.getNotes();
        return res;
    }

    // ── Getters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getFromSlotId() { return fromSlotId; }
    public Long getToSlotId() { return toSlotId; }
    public AssetType getAssetType() { return assetType; }
    public Long getAssetId() { return assetId; }
    public String getPerformedBy() { return performedBy; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public String getNotes() { return notes; }
}
