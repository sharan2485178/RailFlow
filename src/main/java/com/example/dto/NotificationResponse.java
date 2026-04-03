package com.example.dto;

import com.example.model.Notification;
import com.example.model.NotificationCategory;
import com.example.model.NotificationEntityType;
import com.example.model.NotificationStatus;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private Long userId;
    private String role;
    private String message;
    private Long entityId;
    private NotificationEntityType entityType;
    private NotificationCategory category;
    private NotificationStatus status;
    private LocalDateTime createdAt;

    public static NotificationResponse fromNotification(Notification n) {
        NotificationResponse res = new NotificationResponse();
        res.id = n.getId();
        res.userId = n.getUserId();
        res.role = n.getRole();
        res.message = n.getMessage();
        res.entityId = n.getEntityId();
        res.entityType = n.getEntityType();
        res.category = n.getCategory();
        res.status = n.getStatus();
        res.createdAt = n.getCreatedAt();
        return res;
    }

    // ── Getters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
    public Long getEntityId() { return entityId; }
    public NotificationEntityType getEntityType() { return entityType; }
    public NotificationCategory getCategory() { return category; }
    public NotificationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
