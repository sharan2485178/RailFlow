package com.example.mapper;

import com.example.dto.WagonRequest;
import com.example.dto.WagonResponse;
import com.example.enums.AssetOperationalStatus;
import com.example.model.Wagon;
import org.springframework.stereotype.Component;

@Component
public class WagonMapper {

    // WagonRequest → Wagon entity
    public Wagon toEntity(WagonRequest req) {
        Wagon wagon = new Wagon();
        wagon.setType(req.getType());
        wagon.setCapacityTon(req.getCapacityTon());
        wagon.setSerialNumber(req.getSerialNumber());
        wagon.setStatus(AssetOperationalStatus.AVAILABLE);
        return wagon;
    }

    // Wagon entity → WagonResponse
    public WagonResponse toResponse(Wagon wagon) {
        return new WagonResponse(
            wagon.getId(),
            wagon.getType(),
            wagon.getCapacityTon(),
            wagon.getSerialNumber(),
            wagon.getStatus()
        );
    }
}