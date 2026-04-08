package com.example.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.LocomotiveAssignmentRequest;
import com.example.dto.LocomotiveAssignmentResponse;
import com.example.enums.AssetOperationalStatus;
import com.example.enums.TimetableStatus;
import com.example.mapper.LocomotiveAssignmentMapper;
import com.example.model.LocomotiveAssignment;
import com.example.repository.LocomotiveAssignmentRepository;
import com.example.repository.TimetableRepository;
import com.example.service.LocomotiveAssignmentService;

@Service
public class LocomotiveAssignmentServiceImpl implements LocomotiveAssignmentService {

    private final LocomotiveAssignmentRepository locomotiveAssignmentRepository;
    private final LocomotiveAssignmentMapper locomotiveAssignmentMapper;
    private final TimetableRepository timetableRepository;

    public LocomotiveAssignmentServiceImpl(
            LocomotiveAssignmentRepository locomotiveAssignmentRepository,
            LocomotiveAssignmentMapper locomotiveAssignmentMapper,
            TimetableRepository timetableRepository) {
        this.locomotiveAssignmentRepository = locomotiveAssignmentRepository;
        this.locomotiveAssignmentMapper = locomotiveAssignmentMapper;
        this.timetableRepository = timetableRepository;
    }

    @Transactional
    public LocomotiveAssignmentResponse assign(LocomotiveAssignmentRequest req) {

        // timetable must be PUBLISHED
        timetableRepository.findById(req.getTimetableId())
                .filter(t -> t.getStatus() == TimetableStatus.PUBLISHED)
                .orElseThrow(() -> new IllegalStateException(
                        "Timetable must be PUBLISHED before assigning locomotive"));

        // one locomotive per timetable
        if (locomotiveAssignmentRepository.existsByTimetableId(
                req.getTimetableId())) {
            throw new IllegalStateException(
                    "Timetable " + req.getTimetableId()
                    + " already has a locomotive assigned");
        }

        // mapper validates locomotive exists
        LocomotiveAssignment assignment =
                locomotiveAssignmentMapper.toEntity(req);

        // locomotive must be available
        if (assignment.getLocomotive().getStatus()
                != AssetOperationalStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Locomotive is not available. Status: "
                    + assignment.getLocomotive().getStatus());
        }

        locomotiveAssignmentRepository.save(assignment);
        return locomotiveAssignmentMapper.toDto(assignment);
    }
}
