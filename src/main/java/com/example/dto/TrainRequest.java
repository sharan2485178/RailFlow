package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainRequest {
    @NotBlank private String number;
    @NotBlank private String operator;
    @NotBlank private String origin;
    @NotBlank private String destination;
    
	
    
}
