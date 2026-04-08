package com.example.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.dto.MaintenanceStatusRequest;
import com.example.enums.MaintenanceStatus;
import com.example.mapper.MaintenanceMapper;
import com.example.model.MaintenanceRecord;
import com.example.repository.MaintenanceRepository;
import com.example.security.AuditService;
import com.example.service.MaintenanceRecordService;

@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    private final MaintenanceRepository maintenanceRepository;
    private final AuditService auditService;
    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceRecordServiceImpl(MaintenanceRepository maintenanceRepository,
                                        AuditService auditService,
                                        MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.auditService = auditService;
        this.maintenanceMapper = maintenanceMapper;
    }

    public MaintenanceResponse create(MaintenanceRequest req, String performedBy) {
        MaintenanceRecord record = maintenanceMapper.toEntity(req, performedBy);
        maintenanceRepository.save(record);

        auditService.log("CREATE_MAINTENANCE_RECORD", "MaintenanceRecord", record.getId().toString(),
                performedBy, "Maintenance record created for " + req.getAssetType()
                        + " id=" + req.getAssetId());

        return maintenanceMapper.toResponse(record);
    }

    public List<MaintenanceResponse> getAll(String status, Long timetableId) {
        List<MaintenanceRecord> records;

        if (timetableId != null) {
            records = maintenanceRepository.findByTimetableId(timetableId);
        } else if (status != null && !status.isBlank()) {
            records = maintenanceRepository.findByStatus(
                    MaintenanceStatus.valueOf(status.toUpperCase()));
        } else {
            records = maintenanceRepository.findAll();
        }

        return records.stream()
                .map(maintenanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MaintenanceResponse getById(Long id) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found: " + id));
        return maintenanceMapper.toResponse(record);
    }

    public MaintenanceResponse changeStatus(Long id, MaintenanceStatusRequest req, String performedBy) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found: " + id));

        MaintenanceStatus oldStatus = record.getStatus();
        record.setStatus(req.getStatus());

        if (req.getNotes() != null && !req.getNotes().isBlank()) {
            record.setNotes(req.getNotes());
        }
        if (req.getStatus() == MaintenanceStatus.COMPLETED) {
            record.setCompletedAt(LocalDateTime.now());
        }

        maintenanceRepository.save(record);

        auditService.log("CHANGE_MAINTENANCE_STATUS", "MaintenanceRecord", id.toString(),
                performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());

        return maintenanceMapper.toResponse(record);
    }
}