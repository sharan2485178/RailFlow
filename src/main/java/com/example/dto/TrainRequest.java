package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainRequest {
    @NotBlank private String number;
    @NotBlank private String operator;
    @NotBlank private String origin;
    @NotBlank private String destination;
}
