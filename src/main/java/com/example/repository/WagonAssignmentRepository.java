package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetAssignmentStatus;
import com.example.model.WagonAssignment;
@Repository
public interface WagonAssignmentRepository extends JpaRepository<WagonAssignment,Long>{
	List<WagonAssignment> findByTimetableId(Long timetableId);
	boolean existsByBookingIdAndTimetableId(
            Long bookingId, Long timetableId);
	
	boolean existsByWagonIdAndAssetOperationalStatusIn(Long wagonId,List<AssetAssignmentStatus> statuses);


}
