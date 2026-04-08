package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;
import com.example.enums.InspectionStatus;
import com.example.mapper.InspectionMapper;
import com.example.model.InspectionRecord;
import com.example.repository.InspectionRepository;
import com.example.security.AuditService;
import com.example.service.InspectionService;

@Service
public class InspectionServiceImpl implements InspectionService {

    private final InspectionRepository inspectionRepository;
    private final AuditService auditService;
    private final InspectionMapper inspectionMapper;

    public InspectionServiceImpl(InspectionRepository inspectionRepository,
                                 AuditService auditService,
                                 InspectionMapper inspectionMapper) {
        this.inspectionRepository = inspectionRepository;
        this.auditService = auditService;
        this.inspectionMapper = inspectionMapper;
    }

    public InspectionResponse create(InspectionRequest req, String performedBy) {
        InspectionRecord record = inspectionMapper.toEntity(req);
        inspectionRepository.save(record);

        auditService.log("CREATE_INSPECTION", "InspectionRecord", record.getId().toString(),
                performedBy, "Inspection created for " + req.getAssetType()
                        + " id=" + req.getAssetId() + " result=" + req.getResult());

        return inspectionMapper.toResponse(record);
    }

    public List<InspectionResponse> getAll(String result, Long timetableId) {
        List<InspectionRecord> records;

        if (timetableId != null) {
            records = inspectionRepository.findByTimetableId(timetableId);
        } else if (result != null && !result.isBlank()) {
            records = inspectionRepository.findByResult(
                    InspectionStatus.valueOf(result.toUpperCase()));
        } else {
            records = inspectionRepository.findAll();
        }

        return records.stream()
                .map(inspectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public InspectionResponse getById(Long id) {
        InspectionRecord record = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found: " + id));
        return inspectionMapper.toResponse(record);
    }
}