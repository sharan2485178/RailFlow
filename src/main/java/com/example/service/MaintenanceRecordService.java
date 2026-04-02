package com.example.service;

import com.example.dto.MaintenanceRequest;
import com.example.dto.MaintenanceResponse;
import com.example.dto.MaintenanceStatusRequest;
import com.example.model.MaintenanceRecord;
import com.example.model.MaintenanceStatus;
import com.example.repository.MaintenanceRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceRecordService {

    @Autowired private MaintenanceRepository maintenanceRepository;
    @Autowired private AuditService auditService;

    /**
     * POST /api/maintenance-records
     * Creates a new maintenance log for an asset.
     */
    public MaintenanceResponse create(MaintenanceRequest req, String performedBy) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setTimetableId(req.getTimetableId());
        record.setAssetType(req.getAssetType());
        record.setAssetId(req.getAssetId());
        record.setDescription(req.getDescription());
        record.setPriority(req.getPriority());
        record.setStatus(MaintenanceStatus.OPEN);
        record.setAssignedTo(req.getAssignedTo());
        record.setReportedBy(performedBy);
        record.setScheduledDate(req.getScheduledDate());
        record.setNotes(req.getNotes());
        record.setCreatedAt(LocalDateTime.now());
        maintenanceRepository.save(record);

        auditService.log("CREATE_MAINTENANCE_RECORD", "MaintenanceRecord", record.getId().toString(),
                performedBy, "Maintenance record created for " + req.getAssetType()
                        + " id=" + req.getAssetId());
        return MaintenanceResponse.fromRecord(record);
    }

    /**
     * GET /api/maintenance-records
     * Returns all maintenance records, optionally filtered by status or timetableId.
     */
    public List<MaintenanceResponse> getAll(String status, Long timetableId) {
        List<MaintenanceRecord> records;

        if (timetableId != null) {
            records = maintenanceRepository.findByTimetableId(timetableId);
        } else if (status != null && !status.isBlank()) {
            records = maintenanceRepository.findByStatus(MaintenanceStatus.valueOf(status.toUpperCase()));
        } else {
            records = maintenanceRepository.findAll();
        }

        return records.stream().map(MaintenanceResponse::fromRecord).collect(Collectors.toList());
    }

    /**
     * GET /api/maintenance-records/{id}
     */
    public MaintenanceResponse getById(Long id) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found: " + id));
        return MaintenanceResponse.fromRecord(record);
    }

    /**
     * PUT /api/maintenance-records/{id}/status
     * Updates the status of a maintenance record (e.g. Pending → In-Progress → Completed).
     */
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
        return MaintenanceResponse.fromRecord(record);
    }
}
