package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.NotificationEntityType;
import com.example.enums.TaskStatus;
import com.example.model.Task;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
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

    
}
