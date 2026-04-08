package com.example.dto;

import com.example.enums.TimetableStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimetableStatusRequest {

    @NotNull(message = "Status is required")
    private TimetableStatus status;
}