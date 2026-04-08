package com.example.dto;

import java.math.BigDecimal;

import com.example.enums.AssetOperationalStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class LocomotiveResponse {

    private Long id;
    private String model;
    private BigDecimal capacityTon;
    private String serialNumber;
    private AssetOperationalStatus status;


    public LocomotiveResponse(Long id, String model, BigDecimal capacityTon,
                               String serialNumber, AssetOperationalStatus status) {
        this.id = id;
        this.model = model;
        this.capacityTon = capacityTon;
        this.serialNumber = serialNumber;
        this.status = status;
    }

    
}