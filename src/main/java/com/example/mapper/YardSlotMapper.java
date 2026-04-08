package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.YardSlotRequest;
import com.example.dto.YardSlotResponse;
import com.example.enums.YardSlotStatus;
import com.example.model.Yard;
import com.example.model.YardSlot;

@Component
public class YardSlotMapper {

    // YardSlotRequest → YardSlot entity
    public YardSlot toEntity(YardSlotRequest req, Yard yard) {
        YardSlot slot = new YardSlot();
        slot.setYard(yard);
        slot.setTrackNumber(req.getTrackNumber());
        slot.setPosition(req.getPosition());
        slot.setStatus(YardSlotStatus.AVAILABLE);
        return slot;
    }

    // YardSlot entity → YardSlotResponse
    public YardSlotResponse toResponse(YardSlot slot) {
        YardSlotResponse response = new YardSlotResponse();
        response.setSlotId(slot.getSlotId());
        response.setTrackNumber(slot.getTrackNumber());
        response.setPosition(slot.getPosition());
        response.setStatus(slot.getStatus());
        response.setAssignedAssetType(slot.getAssignedAssetType());
        response.setAssignedAssetId(slot.getAssignedAssetId());
        if (slot.getYard() != null) {
            response.setYardId(slot.getYard().getId());
            response.setYardName(slot.getYard().getName());
        }
        return response;
    }
}