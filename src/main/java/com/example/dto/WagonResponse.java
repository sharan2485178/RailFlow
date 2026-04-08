package com.example.dto;

import com.example.enums.AssetOperationalStatus;
import com.example.enums.WagonType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WagonResponse {

    private Long id;
    private WagonType type;
    private Double capacityTon;
    private String serialNumber;
    private AssetOperationalStatus status;

    

    public WagonResponse(Long id, WagonType type, Double capacityTon,
                         String serialNumber, AssetOperationalStatus status) {
        this.id = id;
        this.type = type;
        this.capacityTon = capacityTon;
        this.serialNumber = serialNumber;
        this.status = status;
    }

   
}