package com.example.dto;

import com.example.enums.AssetOperationalStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WagonStatusRequest {
    @NotNull private AssetOperationalStatus status;

	
    
    
}
