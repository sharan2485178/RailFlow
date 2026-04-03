package com.example.dto;

import com.example.model.NotificationCategory;
import com.example.model.NotificationEntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationCreateRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "role is required")
    private String role;

    @NotBlank(message = "message is required")
    private String message;

    @NotNull(message = "entityId is required")
    private Long entityId;

    @NotNull(message = "entityType is required")
    private NotificationEntityType entityType;

    @NotNull(message = "category is required")
    private NotificationCategory category;

    // ── Getters & Setters ────────────────────────────────────────

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public NotificationEntityType getEntityType() { return entityType; }
    public void setEntityType(NotificationEntityType entityType) { this.entityType = entityType; }

    public NotificationCategory getCategory() { return category; }
    public void setCategory(NotificationCategory category) { this.category = category; }
}
