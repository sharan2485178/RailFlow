package com.example.dto;

import com.example.enums.ManifestStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManifestStatusRequest {
    @NotNull private ManifestStatus status;
}
