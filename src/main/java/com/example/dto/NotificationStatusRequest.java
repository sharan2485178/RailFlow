package com.example.dto;

import com.example.model.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public class NotificationStatusRequest {

    @NotNull(message = "Status is required")
    private NotificationStatus status;

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
}
