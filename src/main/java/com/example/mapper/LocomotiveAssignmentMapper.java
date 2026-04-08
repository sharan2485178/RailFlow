package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.LocomotiveAssignmentRequest;
import com.example.dto.LocomotiveAssignmentResponse;
import com.example.enums.AssetAssignmentStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.Locomotive;
import com.example.model.LocomotiveAssignment;
import com.example.model.Timetable;
import com.example.repository.LocomotiveRepository;
import com.example.repository.TimetableRepository;

@Component
public class LocomotiveAssignmentMapper {

    private final LocomotiveRepository locomotiveRepository;
    private final TimetableRepository timetableRepository;

    public LocomotiveAssignmentMapper(
            LocomotiveRepository locomotiveRepository,
            TimetableRepository timetableRepository) {
        this.locomotiveRepository = locomotiveRepository;
        this.timetableRepository = timetableRepository;
    }

    // request → entity
    public LocomotiveAssignment toEntity(LocomotiveAssignmentRequest req) {

        Timetable timetable = timetableRepository.findById(req.getTimetableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Timetable", req.getTimetableId()));

        Locomotive locomotive = locomotiveRepository.findById(req.getLocomotiveId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Locomotive", req.getLocomotiveId()));

        LocomotiveAssignment assignment = new LocomotiveAssignment();
        assignment.setTimetable(timetable);
        assignment.setLocomotive(locomotive);
        assignment.setStatus(AssetAssignmentStatus.PENDING);
        return assignment;
    }

    // entity → response DTO
    public LocomotiveAssignmentResponse toDto(LocomotiveAssignment assignment) {
        LocomotiveAssignmentResponse res = new LocomotiveAssignmentResponse();
        res.setId(assignment.getId());
        res.setTimetableId(assignment.getTimetable().getId());
        res.setLocomotiveId(assignment.getLocomotive().getId());
        res.setLocomotiveModel(assignment.getLocomotive().getModel());
        res.setSerialNumber(assignment.getLocomotive().getSerialNumber());
        res.setStatus(assignment.getStatus());
        return res;
    }
}