package com.example.service;

import com.example.dto.TrainRequest;
import com.example.dto.TrainStatusRequest;
import com.example.model.Train;
import com.example.model.TrainStatus;
import com.example.repository.TrainRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    @Autowired private TrainRepository trainRepository;
    @Autowired private AuditService auditService;

    public Train register(TrainRequest req, String performedBy) {
        if (trainRepository.existsByNumber(req.getNumber()))
            throw new RuntimeException("Train number already exists: " + req.getNumber());

        Train train = new Train();
        train.setNumber(req.getNumber());
        train.setOperator(req.getOperator());
        train.setOrigin(req.getOrigin());
        train.setDestination(req.getDestination());
        train.setStatus(TrainStatus.ACTIVE);
        trainRepository.save(train);

        auditService.log("REGISTER_TRAIN", "Train", train.getId().toString(),
            performedBy, "Train registered: " + train.getNumber());
        return train;
    }

    public List<Train> getAll(String status) {
        if (status != null && !status.isBlank())
            return trainRepository.findByStatus(TrainStatus.valueOf(status.toUpperCase()));
        return trainRepository.findAll();
    }

    public Train getById(Long id) {
        return trainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Train not found: " + id));
    }

    public Train update(Long id, TrainRequest req, String performedBy) {
        Train train = getById(id);

        if (!train.getNumber().equals(req.getNumber()) && trainRepository.existsByNumber(req.getNumber()))
            throw new RuntimeException("Train number already exists: " + req.getNumber());

        train.setNumber(req.getNumber());
        train.setOperator(req.getOperator());
        train.setOrigin(req.getOrigin());
        train.setDestination(req.getDestination());
        trainRepository.save(train);

        auditService.log("UPDATE_TRAIN", "Train", id.toString(),
            performedBy, "Train updated: " + train.getNumber());
        return train;
    }

    public Train changeStatus(Long id, TrainStatusRequest req, String performedBy) {
        Train train = getById(id);
        TrainStatus oldStatus = train.getStatus();
        train.setStatus(req.getStatus());
        trainRepository.save(train);

        auditService.log("CHANGE_TRAIN_STATUS", "Train", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());
        return train;
    }
}
