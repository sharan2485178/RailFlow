package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shunting_operation")
public class ShuntingOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fromSlotId;

    @Column(nullable = false)
    private Long toSlotId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType assetType;

    @Column(nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private String performedBy;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public ShuntingOperation() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFromSlotId() { return fromSlotId; }
    public void setFromSlotId(Long fromSlotId) { this.fromSlotId = fromSlotId; }

    public Long getToSlotId() { return toSlotId; }
    public void setToSlotId(Long toSlotId) { this.toSlotId = toSlotId; }

    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getPerformedAt() { return performedAt; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt = performedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "ShuntingOperation{id=" + id + ", assetType=" + assetType + ", assetId=" + assetId
                + ", fromSlotId=" + fromSlotId + ", toSlotId=" + toSlotId + "}";
    }
}
