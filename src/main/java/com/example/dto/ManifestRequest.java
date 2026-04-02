package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManifestRequest {
    @NotNull private Long trainId;
    private String bookingIdsJson;
}
