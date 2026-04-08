package com.example.service;

import java.util.List;

import com.example.dto.LocomotiveRequest;
import com.example.dto.LocomotiveResponse;
import com.example.dto.LocomotiveStatusRequest;

public interface LocomotiveService {
    LocomotiveResponse register(LocomotiveRequest req, String performedBy);
    List<LocomotiveResponse> getAll(String status);
    LocomotiveResponse getById(Long id);
    LocomotiveResponse update(Long id, LocomotiveRequest req, String performedBy);
    LocomotiveResponse changeStatus(Long id, LocomotiveStatusRequest req, String performedBy);
}
