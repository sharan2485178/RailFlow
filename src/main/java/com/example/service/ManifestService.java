package com.example.service;

import java.util.List;

import com.example.dto.AddBookingToManifestRequest;
import com.example.dto.ManifestBookingResponse;
import com.example.dto.ManifestRequest;
import com.example.dto.ManifestResponse;

public interface ManifestService {
    ManifestResponse create(ManifestRequest req, String createdBy);
    List<ManifestResponse> getAll();
    ManifestResponse getById(Long id);
    ManifestBookingResponse addBooking(Long manifestId, AddBookingToManifestRequest req);
    List<ManifestBookingResponse> getBookings(Long manifestId);
}
