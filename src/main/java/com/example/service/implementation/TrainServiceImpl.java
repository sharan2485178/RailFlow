package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.TrainRequest;
import com.example.dto.TrainResponse;
import com.example.dto.TrainStatusRequest;
import com.example.enums.TrainStatus;
import com.example.exception.EntityAlreadyExistException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.TrainMapper;
import com.example.model.Train;
import com.example.repository.TrainRepository;
import com.example.security.AuditService;
import com.example.service.TrainService;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired private TrainRepository trainRepository;
    @Autowired private AuditService auditService;
    @Autowired private TrainMapper trainMapper;

    public TrainResponse register(TrainRequest req, String performedBy) {
        if (trainRepository.existsByNumber(req.getNumber()))
            throw new EntityAlreadyExistException("Train",Long.parseLong(req.getNumber()));

        Train train = trainMapper.toEntity(req);
        trainRepository.save(train);
        auditService.log("REGISTER_TRAIN", "Train", train.getId().toString(),
            performedBy, "Train registered: " + train.getNumber());

        return trainMapper.toResponse(train);
    }

    public List<TrainResponse> getAll(String status) {
        List<Train> trains = (status != null && !status.isBlank())
            ? trainRepository.findByStatus(TrainStatus.valueOf(status.toUpperCase())) //converting String to enum
            : trainRepository.findAll();

        return trains.stream()
            .map(trainMapper::toResponse)
            .collect(Collectors.toList());
    } 

    public TrainResponse getById(Long id) {
        Train train = trainRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Train not found: " + id));
        return trainMapper.toResponse(train);
    }

    public TrainResponse update(Long id, TrainRequest req, String performedBy) {
        Train train = trainRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Train not found: " + id));

        if (!train.getNumber().equals(req.getNumber()) && trainRepository.existsByNumber(req.getNumber()))
            throw new RuntimeException("Train number already exists: " + req.getNumber());

        train.setNumber(req.getNumber());
        train.setOperator(req.getOperator());
        train.setOrigin(req.getOrigin());
        train.setDestination(req.getDestination());
        trainRepository.save(train);
        auditService.log("UPDATE_TRAIN", "Train", id.toString(),
            performedBy, "Train updated: " + train.getNumber());

        return trainMapper.toResponse(train);
    }

    public TrainResponse changeStatus(Long id, TrainStatusRequest req, String performedBy) {
        Train train = trainRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Train not found: " + id));

        TrainStatus oldStatus = train.getStatus();
        train.setStatus(req.getStatus());
        trainRepository.save(train);
        auditService.log("CHANGE_TRAIN_STATUS", "Train", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());

        return trainMapper.toResponse(train);
    }
}