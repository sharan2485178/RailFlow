package com.example.service;

import java.util.List;

import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.dto.TimetableResponse;
import com.example.dto.TimetableStatusRequest;
import com.example.dto.TimetableUpdateRequest;
import com.example.model.Timetable;

public interface TimetableService {
    TimetableResponse create(TimetableRequest req, String perfomedBy);
    List<TimetableResponse> getAll(String status);
    Timetable getById(Long id);
    TimetableAssetsResponse getAssets(Long id);
    TimetableResponse update(Long id, TimetableUpdateRequest req, String performedBy);
    TimetableResponse updateStatus(Long id, TimetableStatusRequest req, String performedBy);
}
