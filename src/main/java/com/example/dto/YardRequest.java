package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class YardRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotNull
    private Integer totalSlots;

    // ── Getters & Setters ────────────────────────────────────────

    
}
