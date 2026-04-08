package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.TrainRequest;
import com.example.dto.TrainResponse;
import com.example.enums.TrainStatus;
import com.example.model.Train;

@Component
public class TrainMapper {

    // TrainRequest → Train entity
    public Train toEntity(TrainRequest req) {
        Train train = new Train();
        train.setNumber(req.getNumber());
        train.setOperator(req.getOperator());
        train.setOrigin(req.getOrigin());
        train.setDestination(req.getDestination());
        train.setStatus(TrainStatus.ACTIVE);
        return train;
    }

    // Train entity → TrainResponse
    public TrainResponse toResponse(Train train) {
        return new TrainResponse(
            train.getId(),
            train.getNumber(),
            train.getOperator(),
            train.getOrigin(),
            train.getDestination(),
            train.getStatus()
        );
    }
}