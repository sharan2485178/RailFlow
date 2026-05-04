package com.example.service;

import java.util.List;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.dto.WagonAvailableResponse;

public interface WagonAssignmentService {
    WagonAssignmentResponse assign(WagonAssignmentRequest req);
    List<WagonAvailableResponse> getMatchingWagonsforBooking(Long bookingId);
}
