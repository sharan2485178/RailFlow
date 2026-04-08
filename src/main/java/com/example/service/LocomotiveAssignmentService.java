package com.example.service;

import com.example.dto.LocomotiveAssignmentRequest;
import com.example.dto.LocomotiveAssignmentResponse;

public interface LocomotiveAssignmentService {
    LocomotiveAssignmentResponse assign(LocomotiveAssignmentRequest req);
}
