package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.CrewAssignmentRequest;
import com.example.dto.CrewAssignmentResponse;
import com.example.enums.CrewAssignmentStatus;
import com.example.enums.TimetableStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.CrewAssignment;
import com.example.model.Timetable;
import com.example.model.User;
import com.example.repository.TimetableRepository;
import com.example.repository.UserRepository;

@Component
public class CrewAssignmentMapper {

    private final UserRepository userRepository;
    private final TimetableRepository timetableRepository;

    public CrewAssignmentMapper(UserRepository userRepository,
                                 TimetableRepository timetableRepository) {
        this.userRepository = userRepository;
        this.timetableRepository = timetableRepository;
    }

    // request → entity
    public CrewAssignment toEntity(CrewAssignmentRequest req) {

       
        
        Timetable timetable=timetableRepository.findById(req.getTimetableId()).orElseThrow(()->
        new EntityNotFoundException( "Timetable", req.getTimetableId()));

        // timetable must be PUBLISHED
        if (timetable.getStatus() != TimetableStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Timetable must be PUBLISHED before assigning crew");
        }

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User", req.getUserId()));

        // user role must match crew role
        if (user.getRole() != req.getCrewRole()) {
            throw new IllegalStateException(
                    "User role " + user.getRole()
                    + " does not match crew role " + req.getCrewRole());
        }

        CrewAssignment assignment = new CrewAssignment();
        assignment.setTimetable(timetable);
        assignment.setUser(user);
        assignment.setCrewRole(req.getCrewRole());
        assignment.setStatus(CrewAssignmentStatus.PENDING);
        return assignment;
    }

    // entity → response DTO
    public CrewAssignmentResponse toDto(CrewAssignment assignment) {
        CrewAssignmentResponse res = new CrewAssignmentResponse();
        res.setId(assignment.getId());
        res.setTimetableId(assignment.getTimetable().getId());
        res.setUserId(assignment.getUser().getId());
        res.setUserName(assignment.getUser().getName());
        res.setCrewRole(assignment.getCrewRole());
        res.setStatus(assignment.getStatus());
        return res;
    }
}