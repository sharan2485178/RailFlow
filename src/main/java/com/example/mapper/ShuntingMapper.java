package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.model.ShuntingOperation;

@Component
public class ShuntingMapper {

    public ShuntingOperation toEntity(ShuntingRequest req, String performedBy) {
        ShuntingOperation operation = new ShuntingOperation();
        operation.setFromSlotId(req.getFromSlotId());
        operation.setToSlotId(req.getToSlotId());
        operation.setAssetType(req.getAssetType());
        operation.setAssetId(req.getAssetId());
        operation.setPerformedBy(performedBy);
        operation.setPerformedAt(LocalDateTime.now());
        operation.setNotes(req.getNotes());
        return operation;
    }

    public ShuntingResponse toResponse(ShuntingOperation operation) {
        ShuntingResponse response = new ShuntingResponse();
        response.setId(operation.getId());
        response.setFromSlotId(operation.getFromSlotId());
        response.setToSlotId(operation.getToSlotId());
        response.setAssetType(operation.getAssetType());
        response.setAssetId(operation.getAssetId());
        response.setPerformedBy(operation.getPerformedBy());
        response.setPerformedAt(operation.getPerformedAt());
        response.setNotes(operation.getNotes());
        return response;
    }
}