package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocomotiveRequest {
    @NotBlank private String model;
    @NotNull @Positive private BigDecimal capacityTon;
    @NotBlank private String serialNumber;
}
