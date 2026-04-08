package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.Role;
import com.example.model.CrewAssignment;

@Repository
public interface CrewAssignmentRepository
        extends JpaRepository<CrewAssignment, Long> {

    List<CrewAssignment> findByTimetableId(Long timetableId);

    // one role per timetable
    boolean existsByTimetableIdAndCrewRole(Long timetableId, Role crewRole);

    // find assignment by user for confirm
    Optional<CrewAssignment> findByIdAndUserId(Long id, Long userId);
}