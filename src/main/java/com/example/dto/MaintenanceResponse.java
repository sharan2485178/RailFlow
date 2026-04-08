package com.example.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.enums.AssetType;
import com.example.enums.MaintenancePriority;
import com.example.enums.MaintenanceStatus;
import com.example.model.MaintenanceRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    
}
