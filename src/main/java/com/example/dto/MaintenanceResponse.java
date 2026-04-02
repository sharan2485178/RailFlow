package com.example.dto;

import com.example.model.AssetType;
import com.example.model.MaintenancePriority;
import com.example.model.MaintenanceRecord;
import com.example.model.MaintenanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceResponse {

    private Long id;
    private Long timetableId;
    private AssetType assetType;
    private Long assetId;
    private String description;
    private MaintenancePriority priority;
    private MaintenanceStatus status;
    private String assignedTo;
    private String reportedBy;
    private LocalDate scheduledDate;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;

    public static MaintenanceResponse fromRecord(MaintenanceRecord r) {
        MaintenanceResponse res = new MaintenanceResponse();
        res.id = r.getId();
        res.timetableId = r.getTimetableId();
        res.assetType = r.getAssetType();
        res.assetId = r.getAssetId();
        res.description = r.getDescription();
        res.priority = r.getPriority();
        res.status = r.getStatus();
        res.assignedTo = r.getAssignedTo();
        res.reportedBy = r.getReportedBy();
        res.scheduledDate = r.getScheduledDate();
        res.completedAt = r.getCompletedAt();
        res.notes = r.getNotes();
        res.createdAt = r.getCreatedAt();
        return res;
    }

    // ── Getters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getTimetableId() { return timetableId; }
    public AssetType getAssetType() { return assetType; }
    public Long getAssetId() { return assetId; }
    public String getDescription() { return description; }
    public MaintenancePriority getPriority() { return priority; }
    public MaintenanceStatus getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getReportedBy() { return reportedBy; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
