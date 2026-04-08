package com.example.service;

import java.util.List;

import com.example.dto.PathConflictResponse;
import com.example.dto.ResolveConflictRequest;
import com.example.model.Timetable;

public interface PathConflictService {
    List<PathConflictResponse> getAllUnresolved();
    void detect(Timetable newTimetable);
    public PathConflictResponse resolve(Long id, ResolveConflictRequest req, String resolvedBy);
}
