package com.example.dto;

import com.example.enums.NotificationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class NotificationStatusRequest {

    @NotNull(message = "Status is required")
    private NotificationStatus status;

    
}
