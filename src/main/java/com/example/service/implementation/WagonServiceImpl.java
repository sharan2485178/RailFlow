package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.WagonRequest;
import com.example.dto.WagonResponse;
import com.example.dto.WagonStatusRequest;
import com.example.enums.AssetOperationalStatus;
import com.example.enums.WagonType;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.WagonMapper;
import com.example.model.Wagon;
import com.example.repository.WagonRepository;
import com.example.security.AuditService;
import com.example.service.WagonService;

@Service
public class WagonServiceImpl implements WagonService {

    @Autowired private WagonRepository wagonRepository;
    @Autowired private AuditService auditService;
    @Autowired private WagonMapper wagonMapper;

    public WagonResponse register(WagonRequest req, String performedBy) {
        if (wagonRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new EntityNotFoundException("Wagon",Long.parseLong(req.getSerialNumber()));

        Wagon wagon = wagonMapper.toEntity(req);
        wagonRepository.save(wagon);
        auditService.log("REGISTER_WAGON", "Wagon", wagon.getId().toString(),
            performedBy, "Wagon registered: " + wagon.getSerialNumber());

        return wagonMapper.toResponse(wagon);
    }

    public List<WagonResponse> getAll(String type, String status) {
        List<Wagon> wagons;
        if (type != null && status != null)
            wagons = wagonRepository.findByTypeAndStatus(
                WagonType.valueOf(type.toUpperCase()),
                AssetOperationalStatus.valueOf(status.toUpperCase()));
        else
            wagons = wagonRepository.findAll();

        return wagons.stream()
            .map(wagonMapper::toResponse)
            .collect(Collectors.toList());
    }

    public WagonResponse getById(Long id) {
        Wagon wagon = wagonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Wagon not found: " + id));
        return wagonMapper.toResponse(wagon);
    }

    public WagonResponse update(Long id, WagonRequest req, String performedBy) {
        Wagon wagon = wagonRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Wagon",id));

        if (!wagon.getSerialNumber().equals(req.getSerialNumber())
                && wagonRepository.existsBySerialNumber(req.getSerialNumber()))
            throw new RuntimeException("Serial number already exists: " + req.getSerialNumber());

        wagon.setType(req.getType());
        wagon.setCapacityTon(req.getCapacityTon());
        wagon.setSerialNumber(req.getSerialNumber());
        wagonRepository.save(wagon);
        auditService.log("UPDATE_WAGON", "Wagon", id.toString(),
            performedBy, "Wagon updated: " + wagon.getSerialNumber());

        return wagonMapper.toResponse(wagon);
    }

    public WagonResponse changeStatus(Long id, WagonStatusRequest req, String performedBy) {
        Wagon wagon = wagonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Wagon not found: " + id));

        AssetOperationalStatus oldStatus = wagon.getStatus();
        wagon.setStatus(req.getStatus());
        wagonRepository.save(wagon);
        auditService.log("CHANGE_WAGON_STATUS", "Wagon", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());

        return wagonMapper.toResponse(wagon);
    }
}