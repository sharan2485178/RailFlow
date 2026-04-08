package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.NotificationEntityType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
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

   
}
