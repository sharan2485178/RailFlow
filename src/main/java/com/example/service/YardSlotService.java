package com.example.service;

import com.example.dto.YardSlotRequest;
import com.example.model.Yard;
import com.example.model.YardSlot;
import com.example.repository.YardRepository;
import com.example.repository.YardSlotRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class YardSlotService {

    @Autowired private YardSlotRepository yardSlotRepository;
    @Autowired private YardRepository yardRepository;
    @Autowired private AuditService auditService;
    public YardSlot configure(YardSlotRequest req, String performedBy) {
        if (yardSlotRepository.existsBySlotCode(req.getSlotCode()))
            throw new RuntimeException("Slot code already exists: " + req.getSlotCode());

        Yard yard = yardRepository.findByName(req.getYardName())
                .orElseThrow(() -> new RuntimeException("Yard not found with name: " + req.getYardName()));

        YardSlot slot = new YardSlot();
        slot.setYardId(yard.getId());
        slot.setSlotCode(req.getSlotCode());
        slot.setYardName(req.getYardName());
        slot.setTrackNumber(req.getTrackNumber());
        slot.setCapacity(req.getCapacity());
        slot.setCreatedAt(LocalDateTime.now());
        yardSlotRepository.save(slot);

        auditService.log("CONFIGURE_SLOT", "YardSlot", slot.getId().toString(),
            performedBy, "Yard slot configured: " + req.getSlotCode());
        return slot;
    }

    public List<YardSlot> getSlotMap() {
        return yardSlotRepository.findAll();
    }
}
