package com.example.service;

import java.util.List;

import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.dto.MaintenanceStatusRequest;

public interface MaintenanceRecordService {

    MaintenanceResponse create(MaintenanceRequest req, String performedBy);

    List<MaintenanceResponse> getAll(String status, Long timetableId);

    MaintenanceResponse getById(Long id);

    MaintenanceResponse changeStatus(Long id, MaintenanceStatusRequest req, String performedBy);
}