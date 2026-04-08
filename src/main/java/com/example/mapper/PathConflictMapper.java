package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.PathConflictResponse;
import com.example.model.PathConflict;

@Component
public class PathConflictMapper {

    public PathConflictResponse toDto(PathConflict conflict) {
        PathConflictResponse res = new PathConflictResponse();
        res.setId(conflict.getId());
        res.setResolutionStatus(conflict.getConflictResolutionStatus().name());
        res.setResolutionNote(conflict.getResolutionNote());
        res.setResolvedBy(conflict.getResolvedBy());
        res.setResolvedAt(conflict.getResolvedAt());
        res.setDetectedAt(conflict.getDetectedAt());
        res.setTimetableId1(conflict.getTimetable1().getId());
        res.setPathCode1(conflict.getTimetable1().getPathCode());
        res.setDepartureTime1(conflict.getTimetable1().getDepartureTime());
        res.setTimetableId2(conflict.getTimetable2().getId());
        res.setPathCode2(conflict.getTimetable2().getPathCode());
        res.setDepartureTime2(conflict.getTimetable2().getDepartureTime());
        return res;
    }
}