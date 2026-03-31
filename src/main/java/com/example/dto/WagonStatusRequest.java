package com.example.dto;

import com.example.model.AssetOperationalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WagonStatusRequest {
    @NotNull private AssetOperationalStatus status;
}
