package com.example.service;

import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveStatusRequest;
import com.example.model.AssetOperationalStatus;
import com.example.model.Locomotive;
import com.example.repository.LocomotiveRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocomotiveService {

    @Autowired private LocomotiveRepository locomotiveRepository;
    @Autowired private AuditService auditService;

    public Locomotive register(LocomotiveRequest req, String performedBy) {
        if (locomotiveRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        Locomotive loco = new Locomotive();
        loco.setModel(req.getModel());
        loco.setCapacityTon(req.getCapacityTon());
        loco.setSerialNumber(req.getSerialNumber());
        loco.setStatus(AssetOperationalStatus.AVAILABLE);
        locomotiveRepository.save(loco);

        auditService.log("REGISTER_LOCOMOTIVE", "Locomotive", loco.getId().toString(),
            performedBy, "Locomotive registered: " + loco.getSerialNumber());
        return loco;
    }

    public List<Locomotive> getAll(String status) {
        if (status != null && !status.isBlank())
            return locomotiveRepository.findByStatus(
                AssetOperationalStatus.valueOf(status.toUpperCase()));
        return locomotiveRepository.findAll();
    }

    public Locomotive getById(Long id) {
        return locomotiveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locomotive not found: " + id));
    }

    public Locomotive update(Long id, LocomotiveRequest req, String performedBy) {
        Locomotive loco = getById(id);

        if (!loco.getSerialNumber().equals(req.getSerialNumber())
                && locomotiveRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        loco.setModel(req.getModel());
        loco.setCapacityTon(req.getCapacityTon());
        loco.setSerialNumber(req.getSerialNumber());
        locomotiveRepository.save(loco);

        auditService.log("UPDATE_LOCOMOTIVE", "Locomotive", id.toString(),
            performedBy, "Locomotive updated: " + loco.getSerialNumber());
        return loco;
    }

    public Locomotive changeStatus(Long id, LocomotiveStatusRequest req, String performedBy) {
        Locomotive loco = getById(id);
        AssetOperationalStatus oldStatus = loco.getStatus();
        loco.setStatus(req.getStatus());
        locomotiveRepository.save(loco);

        auditService.log("CHANGE_LOCOMOTIVE_STATUS", "Locomotive", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());
        return loco;
    }
}
