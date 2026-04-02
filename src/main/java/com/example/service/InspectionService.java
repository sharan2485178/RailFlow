package com.example.service;

import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;
import com.example.model.InspectionRecord;
import com.example.model.InspectionStatus;
import com.example.repository.InspectionRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspectionService {

    @Autowired private InspectionRepository inspectionRepository;
    @Autowired private AuditService auditService;

    /**
     * POST /api/inspections
     * Creates a new inspection record for an asset.
     */
    public InspectionResponse create(InspectionRequest req, String performedBy) {
        InspectionRecord record = new InspectionRecord();
        record.setTimetableId(req.getTimetableId());
        record.setAssetType(req.getAssetType());
        record.setAssetId(req.getAssetId());
        record.setInspectionDate(req.getInspectionDate());
        record.setInspectedBy(req.getInspectedBy());
        record.setResult(req.getResult());
        record.setRemarks(req.getRemarks());
        record.setCreatedAt(LocalDateTime.now());
        inspectionRepository.save(record);

        auditService.log("CREATE_INSPECTION", "InspectionRecord", record.getId().toString(),
                performedBy, "Inspection created for " + req.getAssetType() + " id=" + req.getAssetId()
                        + " result=" + req.getResult());
        return InspectionResponse.fromRecord(record);
    }

    /**
     * GET /api/inspections
     * Returns all inspection records, optionally filtered by result or timetableId.
     */
    public List<InspectionResponse> getAll(String result, Long timetableId) {
        List<InspectionRecord> records;

        if (timetableId != null) {
            records = inspectionRepository.findByTimetableId(timetableId);
        } else if (result != null && !result.isBlank()) {
            records = inspectionRepository.findByResult(InspectionStatus.valueOf(result.toUpperCase()));
        } else {
            records = inspectionRepository.findAll();
        }

        return records.stream().map(InspectionResponse::fromRecord).collect(Collectors.toList());
    }

    /**
     * GET /api/inspections/{id}
     */
    public InspectionResponse getById(Long id) {
        InspectionRecord record = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found: " + id));
        return InspectionResponse.fromRecord(record);
    }
}
