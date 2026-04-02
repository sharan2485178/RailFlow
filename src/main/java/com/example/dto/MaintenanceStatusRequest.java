package com.example.dto;

import com.example.model.MaintenanceStatus;
import jakarta.validation.constraints.NotNull;

public class MaintenanceStatusRequest {

    @NotNull
    private MaintenanceStatus status;

    private String notes;

    // ── Getters & Setters ────────────────────────────────────────

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
