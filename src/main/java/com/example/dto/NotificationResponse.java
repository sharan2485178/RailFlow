package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.NotificationCategory;
import com.example.enums.NotificationEntityType;
import com.example.enums.NotificationStatus;
import com.example.enums.Role;
import com.example.model.Notification;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private Role role;
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

    
   
}
