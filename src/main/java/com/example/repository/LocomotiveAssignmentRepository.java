package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.LocomotiveAssignment;
@Repository
public interface LocomotiveAssignmentRepository extends JpaRepository<LocomotiveAssignment,Long>{
	List<LocomotiveAssignment> findByTimetableId(Long timetableId);
	boolean existsByTimetableId(Long timetableId);

}
