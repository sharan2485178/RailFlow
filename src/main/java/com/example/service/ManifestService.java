package com.example.service;

import com.example.dto.ManifestRequest;
import com.example.dto.ManifestStatusRequest;
import com.example.model.Manifest;
import com.example.model.ManifestStatus;
import com.example.model.Train;
import com.example.repository.ManifestRepository;
import com.example.repository.TrainRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManifestService {

    @Autowired private ManifestRepository manifestRepository;
    @Autowired private TrainRepository trainRepository;
    @Autowired private AuditService auditService;

    public Manifest create(ManifestRequest req, String performedBy) {
        Train train = trainRepository.findById(req.getTrainId())
            .orElseThrow(() -> new RuntimeException("Train not found: " + req.getTrainId()));

        Manifest manifest = new Manifest();
        manifest.setTrain(train);
        manifest.setBookingIdsJson(req.getBookingIdsJson());
        manifest.setCreatedBy(performedBy);
        manifest.setCreatedAt(LocalDateTime.now());
        manifest.setStatus(ManifestStatus.DRAFT);
        manifestRepository.save(manifest);

        auditService.log("CREATE_MANIFEST", "Manifest", manifest.getId().toString(),
            performedBy, "Manifest created for train: " + train.getNumber());
        return manifest;
    }

    public List<Manifest> getAll(String status) {
        if (status != null && !status.isBlank())
            return manifestRepository.findByStatus(ManifestStatus.valueOf(status.toUpperCase()));
        return manifestRepository.findAll();
    }

    public Manifest getById(Long id) {
        return manifestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manifest not found: " + id));
    }

    public List<Manifest> getByTrain(Long trainId) {
        return manifestRepository.findByTrainId(trainId);
    }

    public Manifest update(Long id, ManifestRequest req, String performedBy) {
        Manifest manifest = getById(id);
        Train train = trainRepository.findById(req.getTrainId())
            .orElseThrow(() -> new RuntimeException("Train not found: " + req.getTrainId()));

        manifest.setTrain(train);
        manifest.setBookingIdsJson(req.getBookingIdsJson());
        manifestRepository.save(manifest);

        auditService.log("UPDATE_MANIFEST", "Manifest", id.toString(),
            performedBy, "Manifest updated for train: " + train.getNumber());
        return manifest;
    }

    public Manifest changeStatus(Long id, ManifestStatusRequest req, String performedBy) {
        Manifest manifest = getById(id);
        ManifestStatus oldStatus = manifest.getStatus();
        manifest.setStatus(req.getStatus());
        manifestRepository.save(manifest);

        auditService.log("CHANGE_MANIFEST_STATUS", "Manifest", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());
        return manifest;
    }
}
