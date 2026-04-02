package com.example.service;

import com.example.dto.AssignSlotRequest;
import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.model.ShuntingOperation;
import com.example.model.YardSlot;
import com.example.model.YardSlotStatus;
import com.example.repository.ShuntingOperationRepository;
import com.example.repository.YardSlotRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShuntingService {

    @Autowired private YardSlotRepository yardSlotRepository;
    @Autowired private ShuntingOperationRepository shuntingOperationRepository;
    @Autowired private AuditService auditService;

    /**
     * POST /api/yards/shunting
     * Moves an asset from one yard slot to another.
     */
    public ShuntingResponse shunt(ShuntingRequest req, String performedBy) {
        YardSlot fromSlot = yardSlotRepository.findById(req.getFromSlotId())
                .orElseThrow(() -> new RuntimeException("Source slot not found: " + req.getFromSlotId()));
        YardSlot toSlot = yardSlotRepository.findById(req.getToSlotId())
                .orElseThrow(() -> new RuntimeException("Destination slot not found: " + req.getToSlotId()));

        // Validate source slot holds the expected asset
        if (!req.getAssetType().equals(fromSlot.getAssignedAssetType())
                || !req.getAssetId().equals(fromSlot.getAssignedAssetId())) {
            throw new RuntimeException(
                    "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                    + " is not currently in slot " + fromSlot.getSlotCode());
        }

        // Validate destination slot is available
        if (toSlot.getStatus() != YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Destination slot " + toSlot.getSlotCode() + " is not available");
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
        ShuntingOperation operation = new ShuntingOperation();
        operation.setFromSlotId(req.getFromSlotId());
        operation.setToSlotId(req.getToSlotId());
        operation.setAssetType(req.getAssetType());
        operation.setAssetId(req.getAssetId());
        operation.setPerformedBy(performedBy);
        operation.setPerformedAt(LocalDateTime.now());
        operation.setNotes(req.getNotes());
        shuntingOperationRepository.save(operation);

        auditService.log("SHUNTING_OPERATION", "ShuntingOperation", operation.getId().toString(),
                performedBy, "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                        + " moved from slot " + fromSlot.getSlotCode() + " to " + toSlot.getSlotCode());

        return ShuntingResponse.fromOperation(operation);
    }

    /**
     * PUT /api/yards/{yardId}/slots/{slotId}/assign
     * Assigns an asset to a specific slot within a yard.
     */
    public YardSlot assignSlot(Long yardId, Long slotId, AssignSlotRequest req, String performedBy) {
        YardSlot slot = findSlotInYard(yardId, slotId);

        if (slot.getStatus() != YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotCode() + " is not available for assignment");
        }

        slot.setAssignedAssetType(req.getAssetType());
        slot.setAssignedAssetId(req.getAssetId());
        slot.setStatus(YardSlotStatus.OCCUPIED);
        yardSlotRepository.save(slot);

        auditService.log("ASSIGN_SLOT", "YardSlot", slotId.toString(),
                performedBy, "Asset " + req.getAssetType() + " id=" + req.getAssetId()
                        + " assigned to slot " + slot.getSlotCode());
        return slot;
    }

    /**
     * PUT /api/yards/{yardId}/slots/{slotId}/release
     * Releases (clears) an asset from a specific slot.
     */
    public YardSlot releaseSlot(Long yardId, Long slotId, String performedBy) {
        YardSlot slot = findSlotInYard(yardId, slotId);

        if (slot.getStatus() == YardSlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotCode() + " is already available (nothing to release)");
        }

        String releasedAsset = slot.getAssignedAssetType() + " id=" + slot.getAssignedAssetId();
        slot.setAssignedAssetType(null);
        slot.setAssignedAssetId(null);
        slot.setStatus(YardSlotStatus.AVAILABLE);
        yardSlotRepository.save(slot);

        auditService.log("RELEASE_SLOT", "YardSlot", slotId.toString(),
                performedBy, "Released " + releasedAsset + " from slot " + slot.getSlotCode());
        return slot;
    }

    /**
     * GET /api/yards/{yardId}/slots
     * Returns all slots belonging to a given yard.
     */
    public List<YardSlot> getSlotsByYard(Long yardId) {
        return yardSlotRepository.findAll().stream()
                .filter(s -> yardId.equals(s.getYardId()))
                .toList();
    }

    // ── Helpers ──────────────────────────────────────────────────

    private YardSlot findSlotInYard(Long yardId, Long slotId) {
        YardSlot slot = yardSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found: " + slotId));
        if (!yardId.equals(slot.getYardId())) {
            throw new RuntimeException("Slot " + slotId + " does not belong to yard " + yardId);
        }
        return slot;
    }
}
