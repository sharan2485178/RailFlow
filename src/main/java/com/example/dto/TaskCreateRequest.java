package com.example.dto;

import com.example.model.NotificationEntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TaskCreateRequest {

    @NotNull(message = "assignedTo (userId) is required")
    private Long assignedTo;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "relatedEntityType is required")
    private NotificationEntityType relatedEntityType;

    @NotNull(message = "relatedEntityId is required")
    private Long relatedEntityId;

    @NotNull(message = "dueDate is required")
    private LocalDateTime dueDate;

    // ── Getters & Setters ────────────────────────────────────────

    public Long getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public NotificationEntityType getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(NotificationEntityType relatedEntityType) { this.relatedEntityType = relatedEntityType; }

    public Long getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
}
