package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.enums.MaintenanceStatus;
import com.example.model.MaintenanceRecord;

@Component
public class MaintenanceMapper {

    public MaintenanceRecord toEntity(MaintenanceRequest req, String reportedBy) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setTimetableId(req.getTimetableId());
        record.setAssetType(req.getAssetType());
        record.setAssetId(req.getAssetId());
        record.setDescription(req.getDescription());
        record.setPriority(req.getPriority());
        record.setStatus(MaintenanceStatus.OPEN);
        record.setAssignedTo(req.getAssignedTo());
        record.setReportedBy(reportedBy);
        record.setScheduledDate(req.getScheduledDate());
        record.setNotes(req.getNotes());
        record.setCreatedAt(LocalDateTime.now());
        return record;
    }

    public MaintenanceResponse toResponse(MaintenanceRecord record) {
        MaintenanceResponse response = new MaintenanceResponse();
        response.setId(record.getId());
        response.setTimetableId(record.getTimetableId());
        response.setAssetType(record.getAssetType());
        response.setAssetId(record.getAssetId());
        response.setDescription(record.getDescription());
        response.setPriority(record.getPriority());
        response.setStatus(record.getStatus());
        response.setAssignedTo(record.getAssignedTo());
        response.setReportedBy(record.getReportedBy());
        response.setScheduledDate(record.getScheduledDate());
        response.setCompletedAt(record.getCompletedAt());
        response.setNotes(record.getNotes());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}