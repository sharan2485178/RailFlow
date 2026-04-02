package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull  private Long userId;
    @NotBlank private String origin;
    @NotBlank private String destination;
    private String cargoDetailsJson;
    @NotNull @Positive private Double weightTon;
}
