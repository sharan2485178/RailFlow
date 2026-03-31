package com.example.service;

import com.example.dto.WagonRequest;
import com.example.dto.WagonStatusRequest;
import com.example.model.AssetOperationalStatus;
import com.example.model.Wagon;
import com.example.model.WagonType;
import com.example.repository.WagonRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WagonService {

    @Autowired private WagonRepository wagonRepository;
    @Autowired private AuditService auditService;

    public Wagon register(WagonRequest req, String performedBy) {
        if (wagonRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        Wagon wagon = new Wagon();
        wagon.setType(req.getType());
        wagon.setCapacityTon(req.getCapacityTon());
        wagon.setSerialNumber(req.getSerialNumber());
        wagon.setStatus(AssetOperationalStatus.AVAILABLE);
        wagonRepository.save(wagon);

        auditService.log("REGISTER_WAGON", "Wagon", wagon.getId().toString(),
            performedBy, "Wagon registered: " + wagon.getSerialNumber());
        return wagon;
    }

    public List<Wagon> getAll(String type, String status) {
        if (type != null && status != null)
            return wagonRepository.findByTypeAndStatus(
                WagonType.valueOf(type.toUpperCase()),
                AssetOperationalStatus.valueOf(status.toUpperCase()));
        if (type != null)
            return wagonRepository.findByType(WagonType.valueOf(type.toUpperCase()));
        if (status != null)
            return wagonRepository.findByStatus(AssetOperationalStatus.valueOf(status.toUpperCase()));
        return wagonRepository.findAll();
    }

    public Wagon getById(Long id) {
        return wagonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Wagon not found: " + id));
    }

    public Wagon update(Long id, WagonRequest req, String performedBy) {
        Wagon wagon = getById(id);

        if (!wagon.getSerialNumber().equals(req.getSerialNumber())
                && wagonRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        wagon.setType(req.getType());
        wagon.setCapacityTon(req.getCapacityTon());
        wagon.setSerialNumber(req.getSerialNumber());
        wagonRepository.save(wagon);

        auditService.log("UPDATE_WAGON", "Wagon", id.toString(),
            performedBy, "Wagon updated: " + wagon.getSerialNumber());
        return wagon;
    }

    public Wagon changeStatus(Long id, WagonStatusRequest req, String performedBy) {
        Wagon wagon = getById(id);
        AssetOperationalStatus oldStatus = wagon.getStatus();
        wagon.setStatus(req.getStatus());
        wagonRepository.save(wagon);

        auditService.log("CHANGE_WAGON_STATUS", "Wagon", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());
        return wagon;
    }
}
