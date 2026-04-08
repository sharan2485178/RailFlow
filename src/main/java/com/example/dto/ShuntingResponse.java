package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.AssetType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShuntingResponse {
    private Long id;
    private Long fromSlotId;
    private Long toSlotId;
    private AssetType assetType;
    private Long assetId;
    private String performedBy;
    private LocalDateTime performedAt;
    private String notes;
}