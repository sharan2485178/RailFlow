package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.AssignSlotRequest;
import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.dto.YardSlotResponse;
import com.example.enums.YardSlotStatus;
import com.example.mapper.ShuntingMapper;
import com.example.mapper.YardSlotMapper;
import com.example.model.ShuntingOperation;
import com.example.model.YardSlot;
import com.example.repository.ShuntingOperationRepository;
import com.example.repository.YardSlotRepository;
import com.example.security.AuditService;
import com.example.service.ShuntingService;

@Service
public class ShuntingServiceImpl implements ShuntingService {

    private final YardSlotRepository yardSlotRepository;
    private final ShuntingOperationRepository shuntingOperationRepository;
    private final AuditService auditService;
    private final ShuntingMapper shuntingMapper;
    private final YardSlotMapper yardSlotMapper;

    public ShuntingServiceImpl(YardSlotRepository yardSlotRepository,
                               ShuntingOperationRepository shuntingOperationRepository,
                               AuditService auditService,
                               ShuntingMapper shuntingMapper,
                               YardSlotMapper yardSlotMapper) {
        this.yardSlotRepository = yardSlotRepository;
        this.shuntingOperationRepository = shuntingOperationRepository;
        this.auditService = auditService;
        this.shuntingMapper = shuntingMapper;
        this.yardSlotMapper = yardSlotMapper;
    }

    public ShuntingResponse shunt(ShuntingRequest req, String performedBy) {
        YardSlot fromSlot = yardSlotRepository.findById(req.getFromSlotId())
                .orElseThrow(() -> new RuntimeException("Source slot not found: " + req.getFromSlotId()));
        YardSlot toSlot = yardSlotRepository.findById(req.getToSlotId())
                .orElseThrow(() -> new RuntimeException("Destination slot not found: " + req.getToSlotId()));

        if (!req.getAssetType().equals(fromSlot.getAssignedAssetType())
                || !req.getAssetId().equals(fromSlot.getAssignedAssetId())) {
            throw new RuntimeException(
                    "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                    + " is not currently in slot " + fromSlot.getSlotId());
        }

        if (toSlot.getStatus() != YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Destination slot " + toSlot.getSlotId() + " is not available");
        }

        // Release source slot
        fromSlot.setAssignedAssetType(null);
        fromSlot.setAssignedAssetId(null);
        fromSlot.setStatus(YardSlotStatus.AVAILABLE);
        yardSlotRepository.save(fromSlot);

        // Occupy destination slot
        toSlot.setAssignedAssetType(req.getAssetType());
        toSlot.setAssignedAssetId(req.getAssetId());
        toSlot.setStatus(YardSlotStatus.OCCUPIED);
        yardSlotRepository.save(toSlot);

        // Record shunting operation
        ShuntingOperation operation = shuntingMapper.toEntity(req, performedBy);
        shuntingOperationRepository.save(operation);

        auditService.log("SHUNTING_OPERATION", "ShuntingOperation", operation.getId().toString(),
                performedBy, "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                        + " moved from slot " + fromSlot.getSlotId() + " to " + toSlot.getSlotId());

        return shuntingMapper.toResponse(operation);
    }

    public YardSlotResponse assignSlot(Long yardId, Long slotId, AssignSlotRequest req, String performedBy) {
        YardSlot slot = findSlotInYard(yardId, slotId);

        if (slot.getStatus() != YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotId() + " is not available for assignment");
        }

        slot.setAssignedAssetType(req.getAssetType());
        slot.setAssignedAssetId(req.getAssetId());
        slot.setStatus(YardSlotStatus.OCCUPIED);
        yardSlotRepository.save(slot);

        auditService.log("ASSIGN_SLOT", "YardSlot", slotId.toString(),
                performedBy, "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                        + " assigned to slot " + slot.getSlotId());

        return yardSlotMapper.toResponse(slot);
    }

    public YardSlotResponse releaseSlot(Long yardId, Long slotId, String performedBy) {
        YardSlot slot = findSlotInYard(yardId, slotId);

        if (slot.getStatus() == YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotId() + " is already available (nothing to release)");
        }

        String releasedAsset = slot.getAssignedAssetType() + " id=" + slot.getAssignedAssetId();
        slot.setAssignedAssetType(null);
        slot.setAssignedAssetId(null);
        slot.setStatus(YardSlotStatus.AVAILABLE);
        yardSlotRepository.save(slot);

        auditService.log("RELEASE_SLOT", "YardSlot", slotId.toString(),
                performedBy, "Released " + releasedAsset + " from slot " + slot.getSlotId());

        return yardSlotMapper.toResponse(slot);
    }

    public List<YardSlotResponse> getSlotsByYard(Long yardId) {
        return yardSlotRepository.findByYardId(yardId)
                .stream()
                .map(yardSlotMapper::toResponse)
                .collect(Collectors.toList());
    }

    private YardSlot findSlotInYard(Long yardId, Long slotId) {
        YardSlot slot = yardSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found: " + slotId));
        if (!yardId.equals(slot.getYard().getId())) {
            throw new RuntimeException("Slot " + slotId + " does not belong to yard " + yardId);
        }
        return slot;
    }
}