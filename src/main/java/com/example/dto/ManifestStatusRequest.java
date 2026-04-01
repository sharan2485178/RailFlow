package com.example.dto;

import com.example.model.ManifestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManifestStatusRequest {
    @NotNull private ManifestStatus status;
}
