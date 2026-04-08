package com.example.dto;

import com.example.enums.TrainStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainStatusRequest {
    @NotNull private TrainStatus status;

	
    
}

