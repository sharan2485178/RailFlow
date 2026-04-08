package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveResponse;
import com.example.dto.LocomotiveStatusRequest;
import com.example.enums.AssetOperationalStatus;
import com.example.mapper.LocomotiveMapper;
import com.example.model.Locomotive;
import com.example.repository.LocomotiveRepository;
import com.example.security.AuditService;
import com.example.service.LocomotiveService;

@Service
public class LocomotiveServiceImpl implements LocomotiveService {

    @Autowired private LocomotiveRepository locomotiveRepository;
    @Autowired private AuditService auditService;
    @Autowired private LocomotiveMapper locomotiveMapper;

    public LocomotiveResponse register(LocomotiveRequest req, String performedBy) {
        if (locomotiveRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        Locomotive loco = locomotiveMapper.toEntity(req);
        locomotiveRepository.save(loco);
        auditService.log("REGISTER_LOCOMOTIVE", "Locomotive", loco.getId().toString(),
            performedBy, "Locomotive registered: " + loco.getSerialNumber());

        return locomotiveMapper.toResponse(loco);
    }

    public List<LocomotiveResponse> getAll(String status) {
        List<Locomotive> locos = (status != null && !status.isBlank())
            ? locomotiveRepository.findByStatus(AssetOperationalStatus.valueOf(status.toUpperCase()))
            : locomotiveRepository.findAll();

        return locos.stream()
            .map(locomotiveMapper::toResponse)
            .collect(Collectors.toList());
    }

    public LocomotiveResponse getById(Long id) {
        Locomotive loco = locomotiveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locomotive not found: " + id));
        return locomotiveMapper.toResponse(loco);
    }

    public LocomotiveResponse update(Long id, LocomotiveRequest req, String performedBy) {
        Locomotive loco = locomotiveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locomotive not found: " + id));

        if (!loco.getSerialNumber().equals(req.getSerialNumber())
                && locomotiveRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        loco.setModel(req.getModel());
        loco.setCapacityTon(req.getCapacityTon());
        loco.setSerialNumber(req.getSerialNumber());
        locomotiveRepository.save(loco);
        auditService.log("UPDATE_LOCOMOTIVE", "Locomotive", id.toString(),
            performedBy, "Locomotive updated: " + loco.getSerialNumber());

        return locomotiveMapper.toResponse(loco);
    }

    public LocomotiveResponse changeStatus(Long id, LocomotiveStatusRequest req, String performedBy) {
        Locomotive loco = locomotiveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locomotive not found: " + id));

        AssetOperationalStatus oldStatus = loco.getStatus();
        loco.setStatus(req.getStatus());
        locomotiveRepository.save(loco);
        auditService.log("CHANGE_LOCOMOTIVE_STATUS", "Locomotive", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());

        return locomotiveMapper.toResponse(loco);
    }
}