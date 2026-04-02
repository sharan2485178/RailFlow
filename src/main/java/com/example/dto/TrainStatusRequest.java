package com.example.dto;

import com.example.model.TrainStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainStatusRequest {
    @NotNull private TrainStatus status;
}
