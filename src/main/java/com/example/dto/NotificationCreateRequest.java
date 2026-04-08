package com.example.dto;

import com.example.enums.NotificationCategory;
import com.example.enums.NotificationEntityType;
import com.example.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class NotificationCreateRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "role is required")
    private Role role;

    @NotBlank(message = "message is required")
    private String message;

    @NotNull(message = "entityId is required")
    private Long entityId;

    @NotNull(message = "entityType is required")
    private NotificationEntityType entityType;

    @NotNull(message = "category is required")
    private NotificationCategory category;

    // ── Getters & Setters ────────────────────────────────────────

    
}
