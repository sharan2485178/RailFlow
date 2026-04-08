package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;
import com.example.model.InspectionRecord;

@Component
public class InspectionMapper {

    public InspectionRecord toEntity(InspectionRequest req) {
        InspectionRecord record = new InspectionRecord();
        record.setTimetableId(req.getTimetableId());
        record.setAssetType(req.getAssetType());
        record.setAssetId(req.getAssetId());
        record.setInspectionDate(req.getInspectionDate());
        record.setInspectedBy(req.getInspectedBy());
        record.setResult(req.getResult());
        record.setRemarks(req.getRemarks());
        record.setCreatedAt(LocalDateTime.now());
        return record;
    }

    public InspectionResponse toResponse(InspectionRecord record) {
        InspectionResponse response = new InspectionResponse();
        response.setId(record.getId());
        response.setTimetableId(record.getTimetableId());
        response.setAssetType(record.getAssetType());
        response.setAssetId(record.getAssetId());
        response.setInspectionDate(record.getInspectionDate());
        response.setInspectedBy(record.getInspectedBy());
        response.setResult(record.getResult());
        response.setRemarks(record.getRemarks());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}