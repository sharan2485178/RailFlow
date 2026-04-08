package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveResponse;
import com.example.enums.AssetOperationalStatus;
import com.example.model.Locomotive;

@Component
public class LocomotiveMapper {

    // LocomotiveRequest → Locomotive entity
    public Locomotive toEntity(LocomotiveRequest req) {
        Locomotive loco = new Locomotive();
        loco.setModel(req.getModel());
        loco.setCapacityTon(req.getCapacityTon());
        loco.setSerialNumber(req.getSerialNumber());
        loco.setStatus(AssetOperationalStatus.AVAILABLE);
        return loco;
    }

    // Locomotive entity → LocomotiveResponse
    public LocomotiveResponse toResponse(Locomotive loco) {
        return new LocomotiveResponse(
            loco.getId(),
            loco.getModel(),
            loco.getCapacityTon(),
            loco.getSerialNumber(),
            loco.getStatus()
        );
    }
}