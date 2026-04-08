package com.example.service;

import java.util.List;

import com.example.dto.InspectionRequest;
import com.example.dto.InspectionResponse;

public interface InspectionService {

    InspectionResponse create(InspectionRequest req, String performedBy);

    List<InspectionResponse> getAll(String result, Long timetableId);

    InspectionResponse getById(Long id);
}