package com.example.service;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;

public interface WagonAssignmentService {
    WagonAssignmentResponse assign(WagonAssignmentRequest req);
}
