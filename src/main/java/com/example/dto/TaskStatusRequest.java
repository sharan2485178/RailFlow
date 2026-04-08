package com.example.dto;

import com.example.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class TaskStatusRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;

    
}
