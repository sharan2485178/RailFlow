package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManifestRequest {
    @NotNull 
    private Long timetableId;

	
    
    
}
