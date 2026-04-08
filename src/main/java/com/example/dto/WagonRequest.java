package com.example.dto;

import com.example.enums.WagonType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WagonRequest {
    @NotNull  private WagonType type;
    @NotNull @Positive private Double capacityTon;
    @NotBlank private String serialNumber;
	
	
	
    
    
}
