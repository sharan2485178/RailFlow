package com.example.dto;

import com.example.model.NotificationEntityType;
import com.example.model.Task;
import com.example.model.TaskStatus;

import java.time.LocalDateTime;

public class TaskResponse {

    private Long id;
    private Long assignedTo;
    private String description;
    private TaskStatus status;
    private Long relatedEntityId;
    private NotificationEntityType relatedEntityType;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    public static TaskResponse fromTask(Task t) {
        TaskResponse res = new TaskResponse();
        res.id = t.getId();
        res.assignedTo = t.getAssignedTo();
        res.description = t.getDescription();
        res.status = t.getStatus();
        res.relatedEntityId = t.getRelatedEntityId();
        res.relatedEntityType = t.getRelatedEntityType();
        res.dueDate = t.getDueDate();
        res.createdAt = t.getCreatedAt();
        return res;
    }

    // ── Getters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getAssignedTo() { return assignedTo; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Long getRelatedEntityId() { return relatedEntityId; }
    public NotificationEntityType getRelatedEntityType() { return relatedEntityType; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
