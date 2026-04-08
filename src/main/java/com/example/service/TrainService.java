package com.example.service;

import java.util.List;

import com.example.dto.TrainRequest;
import com.example.dto.TrainResponse;
import com.example.dto.TrainStatusRequest;

public interface TrainService {
    TrainResponse register(TrainRequest req, String performedBy);
    List<TrainResponse> getAll(String status);
    TrainResponse getById(Long id);
    TrainResponse update(Long id, TrainRequest req, String performedBy);
    TrainResponse changeStatus(Long id, TrainStatusRequest req, String performedBy);
}
