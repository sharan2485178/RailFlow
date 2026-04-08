package com.example.dto;

import com.example.enums.AssetType;
import com.example.enums.YardSlotStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class YardSlotResponse {

    private Long slotId;
    private Long yardId;
    private String yardName;
    private String trackNumber;
    private int position;
    private YardSlotStatus status;
    private AssetType assignedAssetType;
    private Long assignedAssetId;

    

    public YardSlotResponse(Long slotId, Long yardId, String yardName,
                            String trackNumber, int position, YardSlotStatus status,
                            AssetType assignedAssetType, Long assignedAssetId) {
        this.slotId = slotId;
        this.yardId = yardId;
        this.yardName = yardName;
        this.trackNumber = trackNumber;
        this.position = position;
        this.status = status;
        this.assignedAssetType = assignedAssetType;
        this.assignedAssetId = assignedAssetId;
    }

    
}